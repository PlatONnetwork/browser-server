#!/bin/bash
PROFILE=$1
./stop.sh "$PROFILE"
nohup /usr/local/jdk8/bin/java -jar scan-job*.jar --spring.profiles.active="$PROFILE" > /dev/null 2>&1 &
sleep 5
pid=$(ps aux | grep scan-job | grep active="$PROFILE" | grep -v grep | awk '{print $2}')
echo "$pid"
if [ -n "$pid" ]; then
 echo "start success"
else
  echo "retry start ..."
  nohup /usr/local/jdk8/bin/java -jar scan-job*.jar --spring.profiles.active="$PROFILE" > /dev/null 2>&1 &
fi
echo 'scan-job Process List:'
ps -elf|grep scan-job | grep active="$PROFILE"
