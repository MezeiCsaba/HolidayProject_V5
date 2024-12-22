
@echo off
cd /d %~dp0		
start "LeaveSystem App" java -jar holiday.jar
timeout /t 8 /nobreak
start "" http://localhost:8080/







