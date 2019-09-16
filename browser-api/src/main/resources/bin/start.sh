#!/bin/bash
PROFILE=$1
ps -ef|grep -v grep|grep browser-api|grep active=$PROFILE|awk '{print $2}'|xargs kill -9;
nohup java -jar browser-api-0.7.2.0.jar --spring.profiles.active=$PROFILE > /dev/null 2>&1 &
echo 'Api Process List:'
ps -elf|grep browser-api