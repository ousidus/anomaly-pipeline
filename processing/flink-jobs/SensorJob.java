package com.pipeline;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;

import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.sink.KafkaSink;

import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;

public class SensorJob {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source =
                KafkaSource.<String>builder()
                        .setBootstrapServers("kafka:9092")
                        .setTopics("sensor-data")
                        .setGroupId("flink-group")
                        .setStartingOffsets(OffsetsInitializer.latest())
                        .setValueOnlyDeserializer(new SimpleStringSchema())
                        .build();

        DataStream<String> stream =
                env.fromSource(source, 
                        org.apache.flink.api.common.eventtime.WatermarkStrategy.noWatermarks(),
                        "Kafka Source");

        DataStream<String> processed =
                stream.map(s -> s);

        KafkaSink<String> kafkaSink =
                KafkaSink.<String>builder()
                        .setBootstrapServers("kafka:9092")
                        .setRecordSerializer(
                                org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema
                                        .builder()
                                        .setTopic("processed-data")
                                        .setValueSerializationSchema(new SimpleStringSchema())
                                        .build())
                        .build();

        processed.sinkTo(kafkaSink);

        processed.addSink(
                JdbcSink.sink(
                        "INSERT INTO sensor_data(time, temperature, vibration, current, anomaly) VALUES (NOW(),0,0,0,false)",
                        (ps, t) -> {},
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withUrl("jdbc:postgresql://timescaledb:5432/sensors")
                                .withDriverName("org.postgresql.Driver")
                                .withUsername("postgres")
                                .withPassword("postgres")
                                .build()
                )
        );

        env.execute("Sensor Pipeline");
    }
}