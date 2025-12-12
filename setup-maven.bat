@echo off
REM Setup Maven environment variables for this project
set "JAVA_HOME=C:\Users\Elkewidy\.vscode\extensions\redhat.java-1.50.0-win32-x64\jre\21.0.9-win32-x86_64"
set "PATH=C:\Users\Elkewidy\Desktop\apache-maven-3.9.11\bin;%PATH%"

echo Maven environment configured!
echo JAVA_HOME: %JAVA_HOME%
echo Maven version:
mvn --version
