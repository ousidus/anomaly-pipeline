flowchart LR

%% ===== EDGE =====
subgraph Edge Layer
    S[Sensor Simulator]
    MQTT[MQTT Broker]
    T[Telegraf]
    S --> MQTT
    T --> MQTT
end

%% ===== STREAMING =====
subgraph Streaming
    K[Kafka]
end

MQTT --> K

%% ===== PROCESSING =====
subgraph Processing
    F[Flink Jobs]
end

K --> F

%% ===== ML =====
subgraph Machine Learning
    INF[FastAPI Inference Service]
    ML[Model Training + MLflow]
end

F --> INF
INF --> F
F --> ML

%% ===== STORAGE =====
subgraph Storage
    TS[TimescaleDB]
    DL[MinIO Data Lake]
end

F --> TS
F --> DL

%% ===== OBSERVABILITY =====
subgraph Observability
    P[Prometheus]
    G[Grafana]
end

TS --> G
P --> G
F --> P
K --> P
INF --> P