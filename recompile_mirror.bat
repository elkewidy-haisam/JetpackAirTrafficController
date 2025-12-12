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
