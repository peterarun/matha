set fy=%1

cd ..\target
mkdir sales-0.0.1-SNAPSHOT
cd sales-0.0.1-SNAPSHOT
jar -xvf ..\sales-0.0.1-SNAPSHOT.jar
cd ..\

java -cp sales-0.0.1-SNAPSHOT\BOOT-INF\lib\*;sales-0.0.1-SNAPSHOT.jar.original com.matha.sales.PurchaseTxnMigration %fy%
pause

java -cp sales-0.0.1-SNAPSHOT\BOOT-INF\lib\*;sales-0.0.1-SNAPSHOT.jar.original com.matha.sales.SalesTxnMigration %fy%
pause

java -cp sales-0.0.1-SNAPSHOT\BOOT-INF\lib\*;sales-0.0.1-SNAPSHOT.jar.original com.matha.sales.OrderMappTxnMigration %fy%
pause

cd ..\dbScripts
