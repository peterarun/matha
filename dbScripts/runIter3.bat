sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createAddresses.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i insertAddresses.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createAccounts.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i insertAccounts.sql
