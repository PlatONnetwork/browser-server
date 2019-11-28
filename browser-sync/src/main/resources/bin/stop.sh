#!/bin/bash
PROFILE=$1
ps -ef|grep -v grep|grep browser-agent|grep active=$PROFILE|awk '{print $2}'|xargs kill -9;
echo 'Agent Process List:'
ps -elf|grep browser-agent