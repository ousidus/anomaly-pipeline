package com.pipeline.sink;

import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

public class KafkaSinkFactory {

    public static KafkaSink<String> createProcessedSink() {

        return KafkaSink.<String>builder()
                .setBootstrapServers("kafka:9092")
                .setRecordSerializer(
                        org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema
                                .builder()
                                .setTopic("processed-data")
                                .setValueSerializationSchema(new SimpleStringSchema())
                                .build())
                .build();
    }
}