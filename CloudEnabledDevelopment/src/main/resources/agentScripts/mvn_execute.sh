#!/bin/bash

cd $1
mvn clean package
java -jar target/*.jar > $1_execute.log