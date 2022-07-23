#!/bin/env groovy
import groovy.cli.OptionField
import groovy.cli.UnparsedField
import groovy.cli.commons.CliBuilder

import java.lang.reflect.Method
import java.util.jar.JarInputStream

import org.codehaus.groovy.tools.LoaderConfiguration
import org.codehaus.groovy.tools.RootLoader
/*
 * Options definition
 */
@OptionField(shortName = 'h', description = 'Show usage information')
boolean help
@OptionField(shortName = 'm', longName = 'main-class', description = 'The main class to use. Will try to guess it using the main attribute \'Main-Class\' from the jar\'s manifest if not provided')
String mainClass
@OptionField(shortName = 'D', longName = 'define', description = 'Define a system property (can be used many times)')
String[] systemProperties
@UnparsedField
List<String> arguments

CliBuilder cli = new CliBuilder(usage: 'groovexec.groovy [options] /path/to/jar [jar arguments]')

/*
 * Options validation
 */
File jarFile
try {
  cli.parseFromInstance(this, args)
  if (help) {
    cli.usage()
    return
  }
  if (!arguments) {
    throw new IllegalArgumentException("You must provide a jar")
  }
  jarFile = new File(arguments.first())
  if (!jarFile.isFile()) {
    throw new IllegalArgumentException("No file was found at $jarFile")
  }
} catch (IllegalArgumentException e) {
  System.err.println 'Error ' + e.message
  cli.usage()
  System.exit(1)
}

/*
 * Processing
 */
if (systemProperties) {
  systemProperties.each { System.setProperty(*it.split("=")) }
}

if (!mainClass) {
  mainClass = new JarInputStream(jarFile.newInputStream()).with {
    it.manifest?.getMainAttributes()?.getValue('Main-Class')
  }
  if (!mainClass) {
    System.err.println "Couldn't find main class from manifest, you need to explicitly provide it."
    System.exit(2)
  }
}

// add jar to classpath
def lc = new LoaderConfiguration()
lc.addFile(jarFile)
ClassLoader loader = new RootLoader(lc)

Class clazz = loader.loadClass(mainClass)
Method mainMethod = clazz.getMethod('main', new Class[]{String[].class})
mainMethod.invoke(null, new Object[] { arguments.subList(1, arguments.size()) as String[] })