#!/bin/bash

cd $1
mvn clean package
java -jar target/*.jar > /agent/logs/$2_execute.log