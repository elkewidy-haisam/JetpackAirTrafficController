REM ============================================================================
REM temp_rebuild.bat - Builds the temp rebuild artifacts
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that builds the temp rebuild artifacts.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   temp_rebuild.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
set "JAVA_HOME=C:\Users\Elkewidy\.vscode\extensions\redhat.java-1.50.0-win32-x64\jre\21.0.9-win32-x86_64"
set "PATH=C:\Users\Elkewidy\Desktop\apache-maven-3.9.11\bin;%PATH%"
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
mvn clean compile
