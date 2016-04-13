# create container with alpha image with port forwarding 8082
CID=$(sudo docker run -p $1:8080 -td alpha /bin/bash)

#Get the container name 
#CID=$(sudo docker ps -a | awk 'NR==2'| awk '{ print $1 }')

echo $CID
