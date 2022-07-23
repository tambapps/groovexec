# groovexec: Execute JARs without Groovy SDK in them

## Why groovexec
Groovy can be used for scripting, but sometimes you need more than one class, and then your script turns into a 
project. To execute a Groovy project (without an IDE), you would have to compile it. You could compile
the jar, including all the Groovy dependencies (`groovy-all` or specific dependencies like `groovy-json`) and then launch `java -jar my-groovy-project.jar` but
these jar can be quite heavy, as they include the whole Groovy SDK (at least the core of groovy).

If you installed Groovy on your computer, you already have the Groovy SDK. So why bother including it in your project's jar, making it heavier?
Just build a jar containing only your compiled source code (and your other non-Groovy dependencies if any) and then execute it
using your installed Groovy SDK.

groovexec helps you execute such jars, relying on the Groovy SDK installed in your computer.


## How to use
Here is the usage of the script
```text
usage: groovexec [options] /path/to/jar [jar arguments]
 -D,--define <arg>       (Optional) Define a system property (can be used
                         many times)
 -m,--main-class <arg>   (Optional) The main class to use. Will try to
                         guess it using the main attribute 'Main-Class'
                         from the jar's manifest if not provided
```

If you are on Linux, you can install it with the `install.sh` script. It will put the script under the `/bin`
directory of you groovy installation.

### Examples

```shell
groovexec -m jar-without-groovy-sdk.jar --my-project-arg 123
```

Or, if you want to explicitly specify the main class
```shell
# Specifying the main class and some java system properties
groovexec -m my.groovy.project.Main jar-without-groovy-sdk.jar --my-project-arg 123
```
You can also define some Java System properties
```shell
# Specifying the main class and some java system properties
groovexec -D propert1=value1 -D property2=value2 jar-without-groovy-sdk.jar --my-project-arg 123
```

Note that if you don't want to install the groovexec, you could just execute the script with `groovy`.

Use
```shell
groovy groovexec.groovy args...
```

Instead of
```shell
groovexec args...
```

## Excluding Groovy SDK from your JARs
To make your JAR smaller, you would have to exclude Groovy SDK when building it. 

### In Gradle projects
Use the `compileOnly` for all your Groovy dependencies (actually I haven't tested this as I am a Maven guy, so if it doesn't work please, notify me)

E.g.

```groovy
compileOnly group: 'org.apache.groovy', name: 'groovy-all', version: '4.0.3', ext: 'pom'
compileOnly group: 'org.apache.groovy', name: 'groovy-cli-commons', version: '4.0.3'
```

### In Maven projects

Add the `provided` scope to all your Groovy dependencies

E.g.
```xml
<dependencies>
  <dependency>
    <groupId>org.apache.groovy</groupId>
    <artifactId>groovy-all</artifactId>
    <version>4.0.3</version>
    <type>pom</type>
  </dependency>
  <dependency>
    <groupId>org.apache.groovy</groupId>
    <artifactId>groovy-cli-commons</artifactId>
    <version>4.0.3</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
