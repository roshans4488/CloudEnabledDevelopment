#!/bin/bash

cd $1
mvn compile > /agent/logs/$2_compile.log
cat << EOF >> /agent/logs/$2_compile.log
[[End-of-file]]
EOF