#!/bin/bash
PROFILE=$1
./stop.sh "$PROFILE"
nohup _JAVA_PATH_ -jar _PROJECT_NAME_*.jar --spring.profiles.active="$PROFILE" > /dev/null 2>&1 &
sleep 5
pid=$(ps aux | grep _PROJECT_NAME_ | grep active="$PROFILE" | grep -v grep | awk '{print $2}')
echo "$pid"
if [ -n "$pid" ]; then
 echo "start success"
else
  echo "retry start ..."
  nohup _JAVA_PATH_ -jar _PROJECT_NAME_*.jar --spring.profiles.active="$PROFILE" > /dev/null 2>&1 &
fi
echo '_PROJECT_NAME_ Process List:'
ps -elf|grep _PROJECT_NAME_ | grep active="$PROFILE"