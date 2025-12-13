REM ============================================================================
REM simple_compile.bat - Compiles the simple compile components using Maven
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles the simple compile components using maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   simple_compile.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
echo Compiling to check for errors...
call setup-maven.bat > nul 2>&1
mvn clean compile
pause
