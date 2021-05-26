#!/bin/bash
PROFILE=$1
pid=$(ps aux | grep scan-job | grep active="$PROFILE" | grep -v grep | awk '{print $2}')
echo "$pid"
if [ -n "$pid" ]; then
 kill -9 "$pid"
 if [ "$?" -eq 0 ]; then
    echo "kill success"
 else
    echo "kill failed"
 fi
fi
echo 'scan-job Process List:'
ps -elf|grep scan-job | grep active="$PROFILE"