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
