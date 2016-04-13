cp /home/ubuntu/scripts/Dockerfile /home/ubuntu/Dockerfile
echo "Building a docker image with java and maven dependancies"
sudo docker build -t alpha .
