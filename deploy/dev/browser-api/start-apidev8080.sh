nohup java -jar browser-api.jar  --spring.profiles.active=apidev8080 > /dev/null 2>&1 &
nohup java -jar browser-api.jar  --spring.profiles.active=apidev8081 > /dev/null 2>&1 &
ps -elf|grep browser-api
