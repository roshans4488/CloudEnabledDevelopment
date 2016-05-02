# create container with alpha image with port forwarding 8082
# $1 --> Agent
# $2 --> TTY
# $3 --> User application
CID=$(sudo docker run -p $1:49999 -p $2:50000 -p $3:8080 -p $4:7777 -td alpha /bin/bash)

#Get the container name 
#CID=$(sudo docker ps -a | awk 'NR==2'| awk '{ print $1 }')

echo $CID