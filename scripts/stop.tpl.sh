#!/bin/bash
PROFILE=$1
pid=$(ps aux | grep _PROJECT_NAME_ | grep active="$PROFILE" | grep -v grep | awk '{print $2}')
echo "$pid"
if [ -n "$pid" ]; then
 kill -9 "$pid"
 if [ "$?" -eq 0 ]; then
    echo "kill success"
 else
    echo "kill failed"
 fi
fi
echo '_PROJECT_NAME_ Process List:'
ps -elf|grep _PROJECT_NAME_ | grep active="$PROFILE"