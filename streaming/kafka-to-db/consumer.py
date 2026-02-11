import time
import json
import psycopg2
from kafka import KafkaConsumer
from kafka.errors import NoBrokersAvailable
from datetime import datetime

# ---- WAIT FOR KAFKA ----
while True:
    try:
        consumer = KafkaConsumer(
            "sensors.raw",
            bootstrap_servers="kafka:9092",
            value_deserializer=lambda m: json.loads(m.decode("utf-8")),
            auto_offset_reset="earliest",
            group_id="db-writer",
        )
        print("Connected to Kafka")
        break
    except NoBrokersAvailable:
        print("Kafka not ready, retrying...")
        time.sleep(5)


# ---- DB ----
conn = psycopg2.connect(
    host="timescaledb",
    dbname="sensors",
    user="postgres",
    password="postgres"
)
cur = conn.cursor()

# ---- LOOP ----
for msg in consumer:
    data = msg.value

    cur.execute(
        """
        INSERT INTO sensor_data
        (time, temperature, vibration, current, anomaly)
        VALUES (%s,%s,%s,%s,%s)
        """,
        (
            datetime.fromtimestamp(data["timestamp"]),
            data["temperature"],
            data["vibration"],
            data["current"],
            data["anomaly"],
        ),
    )

    conn.commit()