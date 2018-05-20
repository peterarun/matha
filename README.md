# matha

Pre-deployment steps:
Import the data file
Create the user and set permissions
Make sure Java Path is set like set PATH=%PATH%;F:\Installs\jdk1.8.0_161\bin;

Deployment Steps:
Run the selectDate.sql as follows: sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i selectDate.sql
Update the sequence sql files with the data found in output
Run runAll.bat
Run runMains.bat
