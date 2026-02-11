package com.pipeline.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pipeline.model.SensorReading;

import org.apache.flink.api.common.functions.MapFunction;

public class JsonToSensorMap implements MapFunction<String, SensorReading> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public SensorReading map(String value) throws Exception {
        return mapper.readValue(value, SensorReading.class);
    }
}