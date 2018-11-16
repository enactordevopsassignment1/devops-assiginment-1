
DevOps assignment
=================

How to install:
---------------
-Run simple_db.sql in mySql server
-Change application.properties with db credentials
-build simple_web_app (mvn clean install)
-Deploy into tomcat 


How to test:
------------
Go to http://localhost:8080/simple_web_app/rememberme
	-> For the first time it shows (Last Logged Time None)
	-> Next time onwards it will display (Last Logged Time Fri Nov 16 00:49:00 IST 2018)

Notes:
------
This application stores, it's application data in mysql. So it's essential to preserve data saved in mysql
