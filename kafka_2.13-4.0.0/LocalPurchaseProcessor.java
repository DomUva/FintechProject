import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import java.util.*;

public class LocalPurchaseProcessor {

    public static final String INPUT_TOPIC = "transactions-input"; // source of the input stream from Plaid
    public static final String OUTPUT_TOPIC = "transactions-output"; // source of the output stream going to text output for user alerts

    // example set of blacklisted purchase categories
    private static final Set<String> BAD_CATEGORIES = Set.of(
        "GENERAL_MERCHANDISE_PET_SUPPLIES",
        "MEDICAL_VETERINARY_SERVICES",
        "GENERAL_MERCHANDISE_OTHER_GENERAL_MERCHANDISE"
    );

    private static final Set<String> ACCEPTABLE_CURRENCIES = Set.of("USD");
    private static final Set<String> BAD_STORES = Set.of("Starbucks", "McDonald's", "Uber");
    private static final int PURCHASE_MAXIMUM = 1000;

    // map a plaid ID to a bank. Inneffective in sandbox
    private static final Map<String, String> idToInstitution = Map.of(
        "1x1Go8KNkncVdE5Go7jqiLvxmAVAP4uqzVWv7", "Chase",
        "L35WdXnKB8cADzxJKbp5F5Z1Aa6am7Ue7BDzM", "Wells Fargo"
    );

    public static void main(String[] args) {
        Properties props = new Properties();
        // ID of the application
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "transaction-filter-stream");
        // port of the kafka server
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsBuilder builder = new StreamsBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        // want to read actively from a stream
        KStream<String, String> transactions = builder.stream(INPUT_TOPIC);

        KStream<String, String> processed = transactions.flatMap((key, value) -> {
    List<KeyValue<String, String>> results = new ArrayList<>();
    try {
        // do not result in an output when the JSON file is empty - needs improvement such that it handles all bad JSON files
        if (value == null || value.trim().isEmpty()) {
            return results;
        }

        JsonNode node = objectMapper.readTree(value);
        String accountId = node.path("account_id").asText();
        String date = node.path("date").asText();
        String currency = node.path("iso_currency_code").asText();
        String store = node.path("name").asText();
        double amount = Math.abs(node.path("amount").asDouble());

        List<String> categoryList = new ArrayList<>();
        if (node.has("category") && node.get("category").isArray()) {
            for (JsonNode cat : node.get("category")) {
                categoryList.add(cat.asText());
            }
        }
        // read the incoming stream data for keywords
        boolean badCategory = categoryList.stream().anyMatch(BAD_CATEGORIES::contains);
        boolean badStore = BAD_STORES.contains(store);
        boolean badCurrency = !ACCEPTABLE_CURRENCIES.contains(currency);
        boolean tooExpensive = amount > PURCHASE_MAXIMUM;

        String result;
        // check if there are any bad categories in the JSON from Plaid
        if (badCategory || badStore || badCurrency || tooExpensive) {
            List<String> reasons = new ArrayList<>();
            if (badCategory) reasons.add("Blacklisted Purchase Category");
            if (badStore) reasons.add("Blacklisted Store");
            if (badCurrency) reasons.add("Unapproved Currency");
            if (tooExpensive) reasons.add("Exceeds max purchase amount");

            result = String.format("SUSPICIOUS PURCHASE ALERT ON ACCOUNT %s (At store %s for the amount of %.2f %s. Reason: %s)", accountId, store, amount, currency, String.join("; ", reasons));
            results.add(KeyValue.pair(accountId, result));
         } //else {
            //String institution = idToInstitution.getOrDefault(accountId, "Unknown");
            //result = String.format("APPROVED: %s (%s) spent %.2f %s at %s on %s",ccountId, institution, amount, currency, store, date);
       // }

       // results.add(KeyValue.pair(accountId, result));
    }
    return results;
});
        // send the data to the output topic
        processed.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}
