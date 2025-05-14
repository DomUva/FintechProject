Instructins to run code

Steps assume that Kafka and Plaid services have been installed locally or on a server.

To start our Kafka interface you will need to open three terminal sessions (or run in the background):

Session 1: 
  KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
  bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties
  bin/kafka-server-start.sh config/server.properties

Session 2:
  bin/kafka-topics.sh --create     --bootstrap-server localhost:9092     --replication-factor 1     --partitions 1     --topic transactions-input
  bin/kafka-topics.sh --create     --bootstrap-server localhost:9092     --replication-factor 1     --partitions 1     --topic transactions-output     --config cleanup.policy=compact
  bin/kafka-run-class.sh LocalPurchaseProcessor.java 

Session 3:
  bin/kafka-console-consumer.sh   --topic transactions-output   --bootstrap-server localhost:9092   --property print.key=true   --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer   --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer

To reset Kafka and start fresh, close all processes and run the following: 
  rm -rf /tmp/kafka-logs /tmp/kraft-combined-logs



To start our Plaid interface you will need to open 3 terminal sessions (or run in the background):

Session 1:
  node plaidServerNew.js

Session 2:
  node plaidServerNew2.js

Session 3:
  node testSim.js <number of requests> & node testSim2.js <number of requests>


