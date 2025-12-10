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
