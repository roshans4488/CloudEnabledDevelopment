echo "Copying the agent and tty utility files to the $1 container"
sudo docker cp /home/ubuntu/agentScripts $1:/
sudo docker cp /home/ubuntu/ttyUtility $1:/