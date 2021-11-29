# klox
Lox interpreter implemented in Kotlin

## [Gradle Tasks](#heading-gradle_tasks)

### [Assembling](#heading-assembling)

```bash
$ ./gradlew clean assemble
```

### [Linting and Formatting](#heading-linting)

```bash
$ ./gradlew lintKotlin
$ ./gradlew formatKotlin
```

### [Running the Interpreter](#heading-running)

After executing the assembly task (see: *[Assembling](#heading-assembling)*)

```bash
$ java -jar build/libs/shadow-klox-1.0-SNAPSHOT-all.jar 
```

## External Links

* [GitIgnore.io Template][1]
* [ShadowJar Plugin][2] - used in packaging an uber-jar that can be run without need to calculate classpaths for dependencies
* [Kotlinter Plugin][3] - Linter Plugin


[1]: <https://www.toptal.com/developers/gitignore/api/kotlin,intellij+all,gradle> (GitIgnore.io Template)
[2]: <https://github.com/johnrengelman/shadow> (ShadowJar Plugin)
[3]: <https://github.com/jeremymailen/kotlinter-gradle> (Kotliner Plugin)