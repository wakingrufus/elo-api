#!/bin/bash
pwd
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}
pwd
bin/elo-api > output.txt 2>&1 & echo $! > pid.txt
