nohup java -jar browser-api-0.6.0-SNAPSHOT.jar  --spring.profiles.active=testa28060 > /dev/null 2>&1 &
nohup java -jar browser-api-0.6.0-SNAPSHOT.jar  --spring.profiles.active=testa28062 > /dev/null 2>&1 &
ps -elf|grep browser-api
