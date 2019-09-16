#!/bin/bash
PROFILE=$1
ps -ef|grep -v grep|grep browser-api|grep active=$PROFILE|awk '{print $2}'|xargs kill -9;
echo 'Api Process List:'
ps -elf|grep browser-api