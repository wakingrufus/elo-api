#!/bin/bash
sleep 30
result=$(curl -s http://localhost:9005/elo-api/health)

if [[ "$result" =~ "OK" ]]; then
    exit 0
else
    curl -vvv -s http://localhost:9005/elo-api/health
    curl -vvv -s http://0.0.0.0:9005/elo-api/health
    exit 1
fi
