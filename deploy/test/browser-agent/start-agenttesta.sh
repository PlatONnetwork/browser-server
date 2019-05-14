nohup java -jar browser-agent.jar --spring.profiles.active=agenttesta > /dev/null 2>&1 &
ps -elf|grep active=agenttesta
