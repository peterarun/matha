sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterBook.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i deleteDup.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i updateBooks.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPublisher.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSupplier.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSchool.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSOrder.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSOrderDet.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createCashBook.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createCashHead.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createDistricts.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createState.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPurchase.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPurDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createPPayment.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPReturn.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterPReturnDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createPTransactions.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSales.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSalesDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createSPayment.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSReturn.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i alterSReturnDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createSTransactions.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i create_Seq_SalesSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i create_Seq_SOrderSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i create_Seq_PurchaseSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i create_Seq_PReturnSeq.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createAddresses.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i insertAddresses.sql

sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i createAccounts.sql
sqlcmd -S .\SQLEXPRESS2014 /d MathaNew /E /i insertAccounts.sql
