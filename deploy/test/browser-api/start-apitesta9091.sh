nohup java -jar browser-api.jar  --spring.profiles.active=apitesta9091 > /dev/null 2>&1 &
ps -elf|grep active=apitesta9091
