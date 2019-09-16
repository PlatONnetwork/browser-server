#!/bin/bash
PROFILE=$1
ps -ef|grep -v grep|grep browser-agent|grep active=$PROFILE|awk '{print $2}'|xargs kill -9;
nohup java -jar browser-agent-0.7.2.0.jar --spring.profiles.active=$PROFILE > /dev/null 2>&1 &
echo 'Agent Process List:'
ps -elf|grep browser-agent