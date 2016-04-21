FROM ubuntu:14.04

RUN apt-get update
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN apt-get install oracle-java8-installer -y
RUN apt-get install oracle-java8-set-default
RUN apt-get install maven -y
RUN apt-get install zip -y
RUN apt-get update
RUN apt-get install nodejs-legacy -y
RUN apt-get install npm -y
RUN echo "deb http://mirrors.kernel.org/ubuntu/ubuntu wily main universe" | sudo tee -a /etc/apt/sources.list
RUN apt-get update
RUN apt-get install tree
RUN apt-get update