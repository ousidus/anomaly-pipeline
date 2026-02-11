CREATE EXTENSION IF NOT EXISTS timescaledb;

CREATE TABLE IF NOT EXISTS sensor_data (
    time TIMESTAMPTZ NOT NULL,
    temperature DOUBLE PRECISION,
    vibration DOUBLE PRECISION,
    current DOUBLE PRECISION,
    anomaly BOOLEAN
);

SELECT create_hypertable('sensor_data', 'time', if_not_exists => TRUE);