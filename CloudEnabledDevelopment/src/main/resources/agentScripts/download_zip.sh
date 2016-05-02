#!/bin/bash

cd /agent/workspace/$2


if [ "$1" == "1" ]
then
	zip -r /download.zip $3
else
	zip /download.zip $3
fi