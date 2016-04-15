echo "Copying the file to the $1 container"
sudo docker cp /home/ubuntu/agentScripts $1:/
# echo "jar file File Transferred"
# echo "Copying the jar execution script"
# sudo docker cp /home/ubuntu/scripts/executejar.sh $1:/executejar.sh
# echo "Script transferred"
