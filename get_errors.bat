REM ============================================================================
REM get_errors.bat - Retrieves get errors information or data
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that retrieves get errors information or data.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   get_errors.bat
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
echo Compiling project to identify errors...
call setup-maven.bat > nul 2>&1
mvn clean compile 2>&1 | tee compile_errors.txt
echo.
echo Output saved to compile_errors.txt
