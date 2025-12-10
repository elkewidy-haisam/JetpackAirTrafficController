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
