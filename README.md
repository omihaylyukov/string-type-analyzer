# String Type Analyzer

**String Type Analyzer** — CLI-приложение, которое считывает строки из файлов и сортирует их в отдельные файлы:  
"floats.txt", "integers.txt", "strings.txt".

## Версии:

* Java 25
* Gradle 9.2.0
* [Picocli 4.7.7](https://mvnrepository.com/artifact/info.picocli/picocli/4.7.7)
* [JUnit Jupiter 6.0.2](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter/6.0.2)
* [Mockito Core 5.21.0](https://mvnrepository.com/artifact/org.mockito/mockito-core/5.21.0)

## Как собрать

Для сборки проекта используйте:
```
./gradlew jar
```

После сборки запустите JAR-файл:
```
java -jar .\build\libs\string-type-analyzer-1.0.0.jar
```

## Параметры запуска
Для ознакомления с параметрами запуска запустите:

```
java -jar string-type-analyzer-1.0.0.jar --help
```

