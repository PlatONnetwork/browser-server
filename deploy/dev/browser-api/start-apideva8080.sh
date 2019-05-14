nohup java -jar browser-api.jar  --spring.profiles.active=apideva8080 > /dev/null 2>&1 &
ps -elf|grep active=apideva
