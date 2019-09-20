set filename=%1

rem set PATH=%PATH%;F:\Installs\jdk1.8.0_161\bin

cd ..\target
mkdir sales-0.0.1-SNAPSHOT
cd sales-0.0.1-SNAPSHOT
jar -xvf ..\sales-0.0.1-SNAPSHOT.jar
cd ..\

echo "Have you updated 169 to 170 ?"
pause

java -cp sales-0.0.1-SNAPSHOT\BOOT-INF\lib\*;sales-0.0.1-SNAPSHOT.jar.original com.matha.sales.SalesCopier %filename%
pause

rmdir sales-0.0.1-SNAPSHOT

cd ..\dbScripts
