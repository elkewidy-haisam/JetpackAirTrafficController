REM ============================================================================
REM setup-maven.bat - Sets up setup maven environment and dependencies
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that sets up setup maven environment and dependencies.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   setup-maven.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
REM Setup Maven environment variables for this project
set "JAVA_HOME=C:\Users\Elkewidy\.vscode\extensions\redhat.java-1.50.0-win32-x64\jre\21.0.9-win32-x86_64"
set "PATH=C:\Users\Elkewidy\Desktop\apache-maven-3.9.11\bin;%PATH%"

echo Maven environment configured!
echo JAVA_HOME: %JAVA_HOME%
echo Maven version:
mvn --version
