Instructins to run code

Steps assume that Kafka and Plaid services have been installed locally or on a server.  

Feel free to  
``` git clone https://github.com/DomUva/FintechProject.git ```

Below is the directory layout we reference in these instructions:
  ```
  sdp3@sdpTeam3:~/CLOUD/kafka_2.13-4.0.0$ ls
  bin  config  fintechpaper.pdf  kafka-instructions.txt  libs  LocalPurchaseProcessor.java  logs  plaid  video_demo.mp4
  sdp3@sdpTeam3:~/CLOUD/kafka_2.13-4.0.0$ ls plaid/
  getAccess1.sh  getPublic1.sh  node_modules  package-lock.json  plaidServer2.js     plaidServerNew.js    server.js    testSim.js  txn1_2.sh  txn3_1.sh  txn5_1.sh
  getAccess2.sh  getPublic2.sh  package.json  plaidServer1.js    plaidServerNew2.js  plaidTransaction.sh  testSim2.js  txn1_1.sh   txn2_1.sh  txn4_1.sh
  sdp3@sdpTeam3:~/CLOUD/kafka_2.13-4.0.0$ 
  ```  

To start our Kafka interface you will need to open three terminal sessions (or run in the background) from within "kafka_2.13-4.0.0":  

Session 1:  
```
  KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"  
  bin/kafka-storage.sh format --standalone -t $KAFKA_CLUSTER_ID -c config/server.properties  
  bin/kafka-server-start.sh config/server.properties  
```
Session 2:  
```
  bin/kafka-topics.sh --create     --bootstrap-server localhost:9092     --replication-factor 1     --partitions 1     --topic transactions-input  
  bin/kafka-topics.sh --create     --bootstrap-server localhost:9092     --replication-factor 1     --partitions 1     --topic transactions-output     --config cleanup.policy=compact  
  bin/kafka-run-class.sh LocalPurchaseProcessor.java   
```
Session 3:  
```
  bin/kafka-console-consumer.sh   --topic transactions-output   --bootstrap-server localhost:9092   --property print.key=true   --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer   --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer  
```
To reset Kafka and start fresh, close all processes and run the following:   
```
  rm -rf /tmp/kafka-logs /tmp/kraft-combined-logs  
```


To start our Plaid interface you will need to open 3 terminal sessions (or run in the background) from within kafka_2.13-4.0.0/plaid:  

Session 1:  
```
  node plaidServerNew.js  
```
Session 2: 
```
  node plaidServerNew2.js  
```
Session 3:  
```
  node testSim.js <number of requests> & node testSim2.js <number of requests>  
```

