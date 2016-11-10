#!/bin/bash
yum -y install java-1.8.0
yum -y remove java-1.7.0-openjdk
export JAVA_HOME=/etc/alternatives/jre