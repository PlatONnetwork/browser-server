nohup java -jar browser-agent.jar --spring.profiles.active=agenttestb > /dev/null 2>&1 &
ps -elf|grep active=agenttestb
