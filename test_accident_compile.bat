REM ============================================================================
REM test_accident_compile.bat - Compiles accident tracking components for testing
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles accident tracking test components using Maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_accident_compile.bat
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
echo Testing accident reporting compilation...
echo.
call setup-maven.bat
call mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ====================================
    echo Compilation successful!
    echo ====================================
    echo.
    echo Changes implemented:
    echo - Added accident reporting to Radio.java
    echo - Created separate accident log files for each city
    echo - Integrated accident reporting into ATC radio communications
    echo.
) else (
    echo.
    echo ====================================
    echo Compilation FAILED!
    echo ====================================
)
pause
