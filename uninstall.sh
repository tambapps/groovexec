#!/bin/bash
# Script to uninstall groovexec for Linux
groovyPath=$(whereis groovy | awk '{print $2}')
if [ -z "$groovyPath" ]
then
      echo "Error: Couldn't find groovy path"
      exit 1
fi
groovyDir=$(dirname $groovyPath)

rm -f $groovyDir/groovexec.groovy $groovyDir/groovexec