package com.example;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducer {

    public static void main(String[] args) {
        String topic = "test-topic";
        String bootstrapServers = "localhost:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (Producer<String, String> producer = new KafkaProducer<>(properties)) {
            for (int i = 1; i <= 10; i++) {
                String message = "Message " + i;
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
                producer.send(record, (metadata, exception) -> {
                    if (exception == null) {
                        System.out.println("Sent: " + message);
                    } else {
                        System.err.println("Error sending message: " + exception.getMessage());
                    }
                });
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
