# groovexec: Execute JARs without Groovy SDK in them

## Why this script
Groovy can be used for scripting, but sometimes you need more than one class, and then your script turns into a 
project. To execute a Groovy project (without an IDE), you would have to compile it. You could compile
the jar, including all the Groovy dependencies (`groovy-all` or specific dependencies like `groovy-json`) and then launch `java -jar my-groovy-project.jar` but
these jar can be quite heavy, as they include the whole Groovy SDK (at least the core of groovy).

But if you installed Groovy on your computer, you already have the Groovy SDK. So why bother including it in your project's jar, make it heavier?
Just build a jar containing only your compiled source code (and maybe some non-Groovy dependencies if any) and then execute it
using your installed Groovy SDK.

This project helps you execute such jars, relying on the Groovy SDK installed in your computer.

## Excluding Groovy SDK from your JARs

In your groovy project, you would have to exclude groovy dependencies when building the jar.

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


## How to use
Here is the usage of the script
```text
usage: groovexec.groovy [options] /path/to/jar [jar arguments]
 -D,--define <arg>       Define a system property (can be used many times)
 -h,--help               Show usage information
 -m,--main-class <arg>   The main class to use. Will try to guess it using
                         the main attribute 'Main-Class' from the jar's
                         manifest if not provided
```

### Examples

```shell
groovy groovexec.groovy -D propert1=value1 -D property2=value2 -m my.groovy.project.Main jar-without-groovy-sdk.jar --my-project-arg 123
```

Note that `groovexec.groovy` is an executable file. If you are on a Linux based Operating System,
you could just call 
```shell
./groovexec.groovy args...
```

Instead of 
```shell
groovy groovexec.groovy args...
```