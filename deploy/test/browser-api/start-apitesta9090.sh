nohup java -jar browser-api.jar  --spring.profiles.active=apitesta9090 > /dev/null 2>&1 &
ps -elf|grep active=apitesta9090
