#!/bin/bash
sleep 30
result=$(curl -s http://localhost:9005/elo-api/health)

if [[ "$result" =~ "OK" ]]; then
    exit 0
else
    exit 1
fi
