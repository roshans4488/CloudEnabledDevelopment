sudo docker exec $1  /bin/sh -c "nohup java -jar /agentScripts/gs-rest-service-0.1.0.jar &"
#sudo docker exec -it $1 bash

#echo "Executing the jar"
#java -jar gs-rest-service-0.1.0.jar