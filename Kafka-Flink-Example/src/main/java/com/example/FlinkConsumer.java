package com.example;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import java.util.Properties;

public class FlinkConsumer {

    public static void main(String[] args) throws Exception {
        String topic = "test-topic";
        String bootstrapServers = "localhost:9092";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", bootstrapServers);
        properties.setProperty("group.id", "test-group");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> stream = env.addSource(
                new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), properties)
        );

        DataStream<Tuple2<String, Integer>> result = stream
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) {
                        return new Tuple2<>(value, value.length());
                    }
                });

        result.print();

        env.execute("Kafka Flink Integration POC");
    }
}
