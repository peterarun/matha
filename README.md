# matha

Pre-deployment steps:
Import the data file
Create the user and set permissions

Deployment Steps:
Run the selectDate.sql as follows: sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i
Update the sequence sql files with the data found in output
Run runAll.bat
Run runMains.bat
