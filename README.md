Use Mtls with Kafka on Openshift
=====================================


Setup Kafka on Openshift with Strimzi 
-------------------------------------

##### 1. Create a new project strimzi-kafka

##### 2. Goto the CRD's and create a Kafka CLuster using the following yaml . 

  https://github.com/abhishekvishnoi/camel-kafka-tls/blob/add5e112098245f7e0d3a4cad726d51ce651d6fe/yamls/kafka-cluster.yaml#L1-L69
    

##### 3. Wait until the Zookeeper and Kafka pods are created  .

##### 4. Gather the following information from the cluster .

   <img src="/images/ca-cert.png" width="60%" height="60%" >

   1. Locate the secret in your project : **my-cluster-cluster-ca-cert**
   2. Save the **user.p12** file .
   3. Note the **user.password** from the secret .



Create a User with Strimzi 
--------------------------

Once you Kafka Cluster is up Create a user using the Operator .

##### 1. Create a user using the following yaml 

  https://github.com/abhishekvishnoi/camel-kafka-tls/blob/add5e112098245f7e0d3a4cad726d51ce651d6fe/yamls/user.yaml#L1-L10

##### 2. Gather the following information from the secret which is created for the user . 

   <img src="/images/user-secret.png" width="60%" height="60%" >

   1. Locate the secret in your project : **my-user**
   2. Save the **user.p12** file .
   3. Note the **user.password** from the secret .


Create the keystore and truststore for the clients 
--------------------------------------------------

##### 1. Create the trutstore using the the information fetched in previous steps . Use the folloing command

   ```
   keytool -import -keystore ca.jks -file ca.crt -alias ca.jks
   ```

##### 2. Create the keystore using the the information fetched in previous steps . Use the folloing command

   ```
   keytool -importkeystore -deststorepass password -destkeypass password -destkeystore user.jks -deststoretype JKS -srckeystore user.p12 -srcstoretype PKCS12 -srcstorepass <your-password>
   ```


Create an Applciation which can produce and consume to a kafka Topic 
--------------------------------------------------------------------


Create simple Application with springboot + Apache Camel to interact with the Kafka Cluster via the user create in the previous steps .


##### 1. Create a route which produces to the Kafka Topic 

  https://github.com/abhishekvishnoi/camel-kafka-tls/blob/add5e112098245f7e0d3a4cad726d51ce651d6fe/src/main/java/com/kafka/kafkacamelexample/KafkaRoute.java#L25-L34



##### 2. Create a route which consumes from  the Kafka Topic 


  https://github.com/abhishekvishnoi/camel-kafka-tls/blob/add5e112098245f7e0d3a4cad726d51ce651d6fe/src/main/java/com/kafka/kafkacamelexample/KafkaRoute.java#L37-L47



Access your Applciation from the CLI via curl
---------------------------------------------

   ```
   curl http://localhost:8988/hello-kafka
   ```

   <img src="/images/app-logs.png" width="60%" height="60%" >
