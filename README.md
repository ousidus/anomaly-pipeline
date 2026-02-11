# Real-Time Anomaly Detection Pipeline

## Overview

A real-time data processing system that ingests industrial sensor data, detects anomalies using machine learning, and visualizes results on operational dashboards.

## Architecture

* Edge ingestion (MQTT)
* Kafka streaming backbone
* Flink processing
* ML-based anomaly detection
* TimescaleDB storage
* Grafana observability

Architecture diagrams are available in `/docs/diagrams`.

## Quick Start

```bash
docker-compose up
```

## Repository Structure

* `edge/` — sensor simulation and data ingestion
* `streaming/` — Kafka configuration and connectors
* `processing/` — Flink jobs
* `ml/` — model training and inference services
* `storage/` — database configuration
* `observability/` — monitoring and dashboards
* `infra/` — local orchestration setup
* `docs/` — documentation

## Goals

* Demonstrate a real-time data pipeline
* Enable predictive maintenance use cases
* Serve as a portfolio / research project
