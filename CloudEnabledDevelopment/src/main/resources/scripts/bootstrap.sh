echo "*********Installing Docker *********"
sudo apt-get update -y
# sudo apt-get install docker.io -y -- installs older version
sudo wget -qO- https://get.docker.com/ | sh
sudo docker --version
echo "*********** end of installation **************"
