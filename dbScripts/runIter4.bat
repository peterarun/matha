sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSupplier.sql
rem sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPurDet.sql
rem sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSalesDet.sql
rem sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSReturn.sql
rem sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSReturnDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPReturn.sql
rem sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPReturnDet.sql
rem sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i create_Seq_PurchaseSeq.sql
rem sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i create_Seq_PReturnSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPurchase.sql

