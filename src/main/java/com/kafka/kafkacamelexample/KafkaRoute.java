package com.kafka.kafkacamelexample;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class KafkaRoute  extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .enableCORS(true)
                .component("jetty")
                .host("0.0.0.0")
                .port(8988)
                .bindingMode(RestBindingMode.json);

        rest()
                .get("/hello-kafka")
                .to("direct:hello-kafka");


        from("direct:hello-kafka")
                .routeId("KafkaGreetingRoute")
                .log("hello")
                .to("kafka:{{topic}}?brokers={{broker}}&sslKeystoreLocation=/home/jboss/kafka.jks" +
                        "&sslKeystorePassword=password" +
                        "&sslKeyPassword=password" +
                        "&securityProtocol=SSL");

        // Kafka Consumer
        from("kafka:{{topic}}?brokers={{broker}}&sslKeystoreLocation=/home/jboss/kafka.jks" +
                "&sslKeystorePassword=password" +
                "&sslKeyPassword=password" +
                "&securityProtocol=SSL")
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}");


    }



}
