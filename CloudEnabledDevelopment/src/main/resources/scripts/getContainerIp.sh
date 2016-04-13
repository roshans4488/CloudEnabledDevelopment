CID=$(sudo docker ps -a | awk 'NR==2'| awk '{ print $1 }')
ip_add=$(sudo docker inspect --format '{{ .NetworkSettings.IPAddress }}' $CID)
echo " for $CID container the ip address = $ip_add"
