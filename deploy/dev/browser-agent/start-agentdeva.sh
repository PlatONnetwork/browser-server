nohup java -jar browser-agent.jar --spring.profiles.active=agentdeva > /dev/null 2>&1 &
ps -elf|grep active=agentdeva
