#!/bin/bash

chmod -R 777 /agent/workspace/*
cd /agent/workspace

mvn archetype:generate -B -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-j2ee-simple -DarchetypeVersion=1.0 -DgroupId=$1 -DartifactId=$2 -Dversion=$3 -Dpackage=$4

chmod -R 755 /agent/workspace