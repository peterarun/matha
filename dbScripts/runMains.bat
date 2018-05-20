cd target
mkdir sales-0.0.1-SNAPSHOT
cd sales-0.0.1-SNAPSHOT
jar -xvf ..\sales-0.0.1-SNAPSHOT.jar
cd ..

java -cp sales-0.0.1-SNAPSHOT\BOOT-INF\lib\*;sales-0.0.1-SNAPSHOT.jar.original com.matha.sales.PurchaseTxnMigration
java -cp sales-0.0.1-SNAPSHOT\BOOT-INF\lib\*;sales-0.0.1-SNAPSHOT.jar.original com.matha.sales.SalesTxnMigration
java -cp sales-0.0.1-SNAPSHOT\BOOT-INF\lib\*;sales-0.0.1-SNAPSHOT.jar.original com.matha.sales.OrderMappTxnMigration

rmdir sales-0.0.1-SNAPSHOT
