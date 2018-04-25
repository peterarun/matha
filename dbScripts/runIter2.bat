sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSTransactions.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPTransactions.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPublisher2.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSchool2.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSOrderDet2.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i updateBooks.sql
