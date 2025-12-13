REM ============================================================================
REM compile_jogl.bat - Compiles the compile jogl components using Maven
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles the compile jogl components using maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   compile_jogl.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
echo Compiling with JOGL support...
cd /d "%~dp0"
call mvn clean compile
if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL - JOGL INTEGRATED
    echo ========================================
    echo.
) else (
    echo.
    echo ========================================
    echo BUILD FAILED - Check errors above
    echo ========================================
    echo.
)
pause
