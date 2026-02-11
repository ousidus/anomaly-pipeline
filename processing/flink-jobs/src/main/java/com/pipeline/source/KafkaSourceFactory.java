package com.pipeline.source;

import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

public class KafkaSourceFactory {

    public static KafkaSource<String> createSensorSource() {

        return KafkaSource.<String>builder()
                .setBootstrapServers("kafka:9092")
                .setTopics("sensor-data")
                .setGroupId("flink-group")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
    }
}