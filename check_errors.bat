@echo off
cd /d "c:\Users\Elkewidy\Desktop\e10b\e10btermproject"
call setup-maven.bat
mvn clean compile > compile_output.txt 2>&1
type compile_output.txt
