#!/bin/bash

cd $1
mvn clean package -Dmaven.test.skip=true > /agent/logs/$2_package.log
java -jar target/*.jar >  /agent/logs/$2_execute.log