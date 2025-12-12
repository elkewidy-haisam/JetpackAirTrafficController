@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
echo Compiling to check for errors...
call setup-maven.bat > nul 2>&1
mvn clean compile
pause
