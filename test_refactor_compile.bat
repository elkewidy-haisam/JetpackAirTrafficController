REM ============================================================================
REM test_refactor_compile.bat - Compiles the test refactor compile components using Maven
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles the test refactor compile components using maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_refactor_compile.bat
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
echo Compiling refactored code...
echo.
call setup-maven.bat
call mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ====================================
    echo Compilation successful!
    echo ====================================
    echo.
    echo Refactoring completed:
    echo - Created CityLogManager.java for log management
    echo - Created JetpackFactory.java for jetpack generation
    echo - Created TimezoneHelper.java for timezone utilities
    echo - Simplified AirTrafficControllerFrame.java
    echo.
) else (
    echo.
    echo ====================================
    echo Compilation FAILED! 
    echo Check errors above.
    echo ====================================
)
pause
