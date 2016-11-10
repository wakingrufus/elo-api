#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}
pid=$(cat "pid.txt")
if [ -e /proc/${pid} ]; then
    kill -9 ${pid}
fi