REM ============================================================================
REM recompile_3d.bat - Compiles the recompile 3d components using Maven
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles the recompile 3d components using maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   recompile_3d.bat
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
echo Recompiling with fixed types...
echo.
call setup-maven.bat > nul 2>&1
mvn compile
echo.
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation completed successfully!
) else (
    echo [ERROR] Compilation failed.
)
pause
