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

    public static final String INPUT_TOPIC = "transactions-input";
    public static final String OUTPUT_TOPIC = "transactions-output";

    private static final Set<String> BAD_CATEGORIES = Set.of(
    "GENERAL_MERCHANDISE_PET_SUPPLIES",
    "MEDICAL_VETERINARY_SERVICES",
    "GENERAL_MERCHANDISE_OTHER_GENERAL_MERCHANDISE",
    "ENTERTAINMENT_CASINOS_AND_GAMBLING",
    "ENTERTAINMENT_VIDEO_GAMES",
    "FOOD_AND_DRINK_BEER_WINE_AND_LIQUOR",
    "FOOD_AND_DRINK_VENDING_MACHINES",
    "GENERAL_MERCHANDISE_GIFTS_AND_NOVELTIES",
    "GENERAL_MERCHANDISE_TOBACCO_AND_VAPE",
    "PERSONAL_CARE_HAIR_AND_BEAUTY",
    "ENTERTAINMENT_OTHER_ENTERTAINMENT",
    "TRAVEL_OTHER_TRAVEL",
    "FOOD_AND_DRINK_FAST_FOOD",
    "GENERAL_MERCHANDISE_ONLINE_MARKETPLACES",
    "ENTERTAINMENT_TV_AND_MOVIES"
    );


    private static final Set<String> ACCEPTABLE_CURRENCIES = Set.of("USD");
    private static final Set<String> BAD_STORES = Set.of("Starbucks", "McDonald's", "Uber");
    private static final int PURCHASE_MAXIMUM = 1000;

    private static final Map<String, String> idToInstitution = Map.of(
        "1x1Go8KNkncVdE5Go7jqiLvxmAVAP4uqzVWv7", "Chase",
        "L35WdXnKB8cADzxJKbp5F5Z1Aa6am7Ue7BDzM", "Wells Fargo",
        "ojBrKa8q3ACaogpzkGAriEq5Mjzj73tpjALwP", "Bank of America"
    );

    public static void main(String[] args) {
        Properties props = new Properties();
        // ID of the application
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "transaction-filter-stream");
        // port of the Kafka server
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsBuilder builder = new StreamsBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        KStream<String, String> transactions = builder.stream(INPUT_TOPIC);
        // want to read actively from a stream
        KStream<String, String> processed = transactions.flatMap((key, value) -> {
    List<KeyValue<String, String>> results = new ArrayList<>();
    try {
        // do not result in an output when the JSON file is empty - needs improvement such that it handles all bad JSON files
        if (value == null || value.trim().isEmpty()) {
            return results; // Empty = skip
        }

        JsonNode node = objectMapper.readTree(value);
        long sent = node.path("send_timestamp").asLong();
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

        boolean badCategory = categoryList.stream().anyMatch(BAD_CATEGORIES::contains);
        boolean badStore = BAD_STORES.contains(store);
        boolean badCurrency = !ACCEPTABLE_CURRENCIES.contains(currency);
        boolean tooExpensive = amount > PURCHASE_MAXIMUM;

        String result;
        if (badCategory || badStore || badCurrency || tooExpensive) {
            List<String> reasons = new ArrayList<>();
            if (badCategory) reasons.add("Blacklisted Purchase Category");
            if (badStore) reasons.add("Blacklisted Store");
            if (badCurrency) reasons.add("Unapproved Currency");
            if (tooExpensive) reasons.add("Exceeds max purchase amount");

            result = String.format("SUSPICIOUS PURCHASE ALERT ON ACCOUNT %s (At store %s for the amount of %.2f %s. Reason: %s)",
                    accountId, store, amount, currency, String.join("; ", reasons));
           long latency = System.currentTimeMillis() - sent; 
           results.add(KeyValue.pair("Round-trip latency: " + latency + " ms", result));;
         } //else {
            //String institution = idToInstitution.getOrDefault(accountId, "Unknown");
            //result = String.format("APPROVED: %s (%s) spent %.2f %s at %s on %s",
              //      accountId, institution, amount, currency, store, date);
       // }

       // results.add(KeyValue.pair(accountId, result));
    } catch (Exception e) {
        // Do nothing if parsing fails
    }
    return results;
});

        processed.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}
