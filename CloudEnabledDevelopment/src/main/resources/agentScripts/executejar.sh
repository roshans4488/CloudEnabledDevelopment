sudo docker exec $1  /bin/sh -c "nohup java -jar /agentScripts/gs-rest-service-0.1.0.jar > /agentScripts/agentlog.out &"