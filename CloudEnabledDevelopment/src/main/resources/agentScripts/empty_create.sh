#!/bin/bash

chmod -R 777 /agent/workspace/*
cd /agent/workspace

mkdir /agent/workspace/$1
chmod -R 755 /agent/workspace/$1