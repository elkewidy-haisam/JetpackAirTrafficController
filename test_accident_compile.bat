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
