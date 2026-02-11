import json
import time
import random
from kafka import KafkaProducer
from kafka.errors import NoBrokersAvailable

# ---- WAIT FOR KAFKA ----
while True:
    try:
        producer = KafkaProducer(
            bootstrap_servers="kafka:9092",
            value_serializer=lambda v: json.dumps(v).encode("utf-8")
        )
        print("Connected to Kafka")
        break
    except NoBrokersAvailable:
        print("Kafka not ready, retrying...")
        time.sleep(5)


TOPIC = "sensors.raw"


def generate():
    anomaly = random.random() < 0.05

    temp = random.uniform(20, 30)
    vibration = random.uniform(0.1, 0.5)
    current = random.uniform(5, 10)

    if anomaly:
        temp += random.uniform(20, 40)
        vibration += random.uniform(2, 5)
        current += random.uniform(10, 20)

    return {
        "timestamp": time.time(),
        "temperature": temp,
        "vibration": vibration,
        "current": current,
        "anomaly": anomaly
    }


while True:
    msg = generate()
    producer.send(TOPIC, msg)
    print(msg)
    time.sleep(1)