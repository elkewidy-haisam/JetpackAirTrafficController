@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
echo Compiling project...
call mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo.
    echo Running application...
    call mvn exec:java -Dexec.mainClass="com.example.Main"
) else (
    echo.
    echo Compilation failed!
)
pause
