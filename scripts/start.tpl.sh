#!/bin/bash
PROFILE=$1
ps -ef|grep -v grep|grep _PROJECT_NAME_|grep active=$PROFILE|awk '{print $2}'|xargs kill -9;
nohup _JAVA_PATH_ -jar _PROJECT_NAME_*.jar --spring.profiles.active=$PROFILE > /dev/null 2>&1 &
echo '_PROJECT_NAME_ Process List:'
ps -elf|grep _PROJECT_NAME_