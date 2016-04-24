#!/bin/bash

cd $1
mvn compile > /agent/logs/$2_compile.log