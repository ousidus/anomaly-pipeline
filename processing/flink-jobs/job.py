from pyflink.datastream import StreamExecutionEnvironment
from pyflink.table import StreamTableEnvironment, EnvironmentSettings

env = StreamExecutionEnvironment.get_execution_environment()
settings = EnvironmentSettings.in_streaming_mode()
t_env = StreamTableEnvironment.create(env, environment_settings=settings)

# Kafka Source Table
t_env.execute_sql("""
CREATE TABLE sensors (
    temperature DOUBLE,
    vibration DOUBLE,
    current DOUBLE,
    anomaly BOOLEAN,
    ts DOUBLE
) WITH (
    'connector' = 'kafka',
    'topic' = 'sensor-data',
    'properties.bootstrap.servers' = 'kafka:9092',
    'format' = 'json',
    'scan.startup.mode' = 'earliest-offset'
)
""")

# JDBC Sink (Timescale/Postgres)
t_env.execute_sql("""
CREATE TABLE sensor_out (
    temperature DOUBLE,
    vibration DOUBLE,
    current DOUBLE,
    anomaly BOOLEAN
) WITH (
    'connector' = 'jdbc',
    'url' = 'jdbc:postgresql://timescaledb:5432/sensors',
    'table-name' = 'sensor_data',
    'username' = 'postgres',
    'password' = 'postgres'
)
""")

# Pipeline query
t_env.execute_sql("""
INSERT INTO sensor_out
SELECT
    temperature,
    vibration,
    current,
    anomaly
FROM sensors
""")