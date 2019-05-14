nohup java -jar browser-agent.jar --spring.profiles.active=agentdevb > /dev/null 2>&1 &
ps -elf|grep active=agentdevb
