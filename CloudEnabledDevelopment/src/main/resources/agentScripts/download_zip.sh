#!/bin/bash

cd /agent/workspace
cd $2

If [ "$1" == "1" ]
then
	zip -r /download.zip $3
else
	zip /download.zip $3
fi