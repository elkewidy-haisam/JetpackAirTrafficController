REM ============================================================================
REM test_compile.bat - Compiles the test compile components using Maven
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles the test compile components using maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_compile.bat
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
echo Compiling project...
call mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo.
    echo Running application...
    call mvn exec:java -Dexec.mainClass="com.example.Main"
) else (
    echo.
    echo Compilation failed!
)
pause
