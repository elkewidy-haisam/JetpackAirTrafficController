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
