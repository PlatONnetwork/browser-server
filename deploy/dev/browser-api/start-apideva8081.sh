nohup java -jar browser-api.jar  --spring.profiles.active=apideva8081 > /dev/null 2>&1 &
ps -elf|grep active=apideva
