# E10B Term Project

A Maven-based Java project.

## Prerequisites

- Java JDK 25 (installed at: `C:\Program Files\Java\jdk-25`)
- Apache Maven 3.9.11 (installed at: `C:\Users\Elkewidy\Desktop\apache-maven-3.9.11`)

## Setup

Before running Maven commands, set up the environment:

```cmd
setup-maven.bat
```

Or manually set the environment variables:

```cmd
set "JAVA_HOME=C:\Program Files\Java\jdk-25"
set "PATH=C:\Users\Elkewidy\Desktop\apache-maven-3.9.11\bin;%PATH%"
```

## Build Commands

**Clean and compile:**
```cmd
mvn clean compile
```

**Run tests:**
```cmd
mvn test
```

**Package the application:**
```cmd
mvn package
```

**Run the application:**
```cmd
mvn exec:java -Dexec.mainClass="com.example.App"
```

## Project Structure

```
e10btermproject/
├── pom.xml
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── example/
│   │               └── App.java
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── AppTest.java
└── README.md
```

## Verification

Maven has been successfully installed and verified. The project builds successfully with Java 25 and Maven 3.9.11.
