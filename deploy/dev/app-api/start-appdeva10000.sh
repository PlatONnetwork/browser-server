nohup java -jar app-api.jar  --spring.profiles.active=appdeva10000 > /dev/null 2>&1 &
ps -elf|grep active=appdeva
