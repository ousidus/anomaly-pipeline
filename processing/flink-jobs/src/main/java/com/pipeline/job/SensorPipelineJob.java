package com.pipeline.job;

import com.pipeline.model.SensorReading;
import com.pipeline.serialization.JsonToSensorMap;
import com.pipeline.source.KafkaSourceFactory;
import com.pipeline.sink.JdbcSinkFactory;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;

public class SensorPipelineJob {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> raw =
                env.fromSource(
                        KafkaSourceFactory.createSensorSource(),
                        WatermarkStrategy.noWatermarks(),
                        "Kafka Source"
                );

        DataStream<SensorReading> readings =
                raw.map(new JsonToSensorMap());

        readings.print(); // debug

        readings.addSink(JdbcSinkFactory.createSink());

        env.execute("Sensor Pipeline");
    }
}