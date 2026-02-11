package com.pipeline.sink;

import com.pipeline.model.SensorReading;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;

import java.sql.Timestamp;

public class JdbcSinkFactory {

    public static org.apache.flink.streaming.api.functions.sink.SinkFunction<SensorReading> createSink() {

        return JdbcSink.sink(

                "INSERT INTO sensor_data(time, temperature, vibration, current, anomaly) VALUES (?,?,?,?,?)",

                (ps, r) -> {
                    ps.setTimestamp(1, new Timestamp((long)(r.timestamp * 1000)));
                    ps.setDouble(2, r.temperature);
                    ps.setDouble(3, r.vibration);
                    ps.setDouble(4, r.current);
                    ps.setBoolean(5, r.anomaly);
                },

                JdbcExecutionOptions.builder()
                        .withBatchSize(100)
                        .withBatchIntervalMs(200)
                        .build(),

                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:postgresql://timescaledb:5432/sensors")
                        .withDriverName("org.postgresql.Driver")
                        .withUsername("postgres")
                        .withPassword("postgres")
                        .build()
        );
    }
}