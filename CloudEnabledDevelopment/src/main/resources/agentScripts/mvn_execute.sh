#!/bin/bash

cd $1
kill -9 $(for i in `netstat -lnp | grep :8080 | awk {'print $7}' | awk -F '/' {'print $1'} | uniq` ; do ps -eo pid | grep $i ; done)
rm -rf /agent/logs/$2_execute.log
mvn clean package -Dmaven.test.skip=true >> /agent/logs/$2_package.log
java -jar target/*.jar >>  /agent/logs/$2_execute.log
cat << EOF >> /agent/logs/$2_execute.log
[[End-of-file]]
EOF