nohup java -jar browser-api.jar  --spring.profiles.active=apitestb9190 > /dev/null 2>&1 &
ps -elf|grep active=apitestb9190
