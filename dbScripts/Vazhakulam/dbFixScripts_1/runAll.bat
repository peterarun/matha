sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i rm_CustId_Constraints.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alter_CustId_Columns.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i redo_CustId_Constraints.sql
