# Book-Library
A simple book library implimented in Java, the instructions for the assignment are found in papers/assignment-partI.pdf.

The library can be run by executing the following commands in the templates directory. The program also includes help instructions on startup.

### To create the jar file

In Windows
```
gradlew.bat build
```

In Unix Based Systems (Linux and MacOS)
```
./gradlew build
```

### To run the jar file (assuming you are in the project root)

In Windows
```
java -jar build\libs\bookLibrary-1.0-SNAPSHOT.jar
```

In Unix Based Systems (Linux and MacOS)
```
java -jar build/libs/bookLibrary-1.0-SNAPSHOT.jar
```

### The tests can also then be run as follows:

In Windows
```
gradlew.bat test
```

In Unix Based Systems (Linux and MacOS)
```
./gradlew test
```
