REM ============================================================================
REM quick_compile_check.bat - Compiles the quick compile check components using Maven
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles the quick compile check components using maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   quick_compile_check.bat
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
echo Compiling with extracted classes...
echo.
call setup-maven.bat
call mvn clean compile 2>&1 | findstr /I /C:"error" /C:"BUILD SUCCESS" /C:"BUILD FAILURE"
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation appears successful!
) else (
    echo.
    echo Checking full output...
)
pause
