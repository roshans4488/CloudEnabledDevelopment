#!/bin/bash

cd $1
kill -9 $(for i in `netstat -lnp | grep :8080 | awk {'print $7}' | awk -F '/' {'print $1'} | uniq` ; do ps -eo pid | grep $i ; done)

