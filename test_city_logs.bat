REM ============================================================================
REM test_city_logs.bat - Executes test city logs to validate functionality
REM ============================================================================
REM Purpose:
REM   Batch script for the Jetpack Air Traffic Controller project that executes test city logs to validate functionality.
REM   Provides a convenient command-line interface for development and testing tasks.
REM
REM Usage:
REM   test_city_logs.bat
REM
REM Requirements:
REM   - Java JDK 11 or higher
REM   - Maven (where applicable)
REM   - Appropriate permissions for file operations
REM
REM Author: Haisam Elkewidy
REM ============================================================================

@echo off
echo Testing city-specific log files...
cd /d "%~dp0"
call mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo.
    echo Expected log files:
    echo - newyork_jetpack_movement_log.txt
    echo - newyork_radar_communications_log.txt
    echo - newyork_weather_broadcast_log.txt
    echo - boston_jetpack_movement_log.txt
    echo - boston_radar_communications_log.txt
    echo - boston_weather_broadcast_log.txt
    echo - houston_jetpack_movement_log.txt
    echo - houston_radar_communications_log.txt
    echo - houston_weather_broadcast_log.txt
    echo - dallas_jetpack_movement_log.txt
    echo - dallas_radar_communications_log.txt
    echo - dallas_weather_broadcast_log.txt
) else (
    echo Compilation failed with errors!
)
pause
