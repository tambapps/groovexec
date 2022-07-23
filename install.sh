#!/bin/bash
# Script to install groovexec for Linux
groovyPath=$(whereis groovy | awk '{print $2}')
if [ -z "$groovyPath" ]
then
      echo "Error: Couldn't find groovy path"
      exit 1
fi
groovyDir=$(dirname $groovyPath)
cp groovexec.groovy $groovyDir || exit 1

groovexecScript="$groovyDir/groovexec"
cat <<EOF > $groovexecScript
#!/bin/bash
groovy groovexec.groovy "\$@"
EOF

chmod u+x $groovexecScript || exit 1
echo "Successfully installed groovexec in $groovexecScript."
echo "Run 'groovexec -h' to test it works"