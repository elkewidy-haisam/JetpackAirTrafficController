REM ============================================================================
REM recompile_mirror.bat - Compiles the recompile mirror components using Maven
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that compiles the recompile mirror components using maven.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   recompile_mirror.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
echo ========================================
echo Recompiling Enhanced Mirror 3D Tracking
echo ========================================
cd /d "%~dp0"
call mvn clean compile -DskipTests
echo.
echo ========================================
echo Compilation complete!
echo ========================================
pause
