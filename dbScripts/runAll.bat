sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterBook.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i deleteDup.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i updateBooks.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterPublisher.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSupplier.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSchool.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSOrder.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSOrderDet.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createCashBook.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createCashHead.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createDistricts.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createState.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterPurchase.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterPurDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createPPayment.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterPReturn.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterPReturnDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createPTransactions.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSales.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSalesDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createSPayment.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSReturn.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i alterSReturnDet.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createSTransactions.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i create_Seq_SalesSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i create_Seq_SOrderSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i create_Seq_PurchaseSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i create_Seq_PReturnSeq.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i create_Seq_SReturnSeq.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createAddresses.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i insertAddresses.sql

sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i createAccounts.sql
sqlcmd -S .\SQLEXPRESS2014 /d Matha /E /i insertAccounts.sql
