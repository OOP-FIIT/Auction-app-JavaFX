# Environment setup

Example:

* Eclipse Java EE IDE for Web Developers, version: Oxygen.3a Release (4.7.3a) Build id: 20180405-1200

(Change this Example:)

* JDK 17
* JavaFX 13
* Scene Builder
* MySQL Connector/J 8.0.23

## Installation
Maven is needed, if it installed then dependencies wiil be download through maven 

Make sure MySQL is installed properly and running at port 3306
To make project compile you should set your LOGIN and PASSWORN inside MySQL
For example I have login: "root" and password: "misha"

Then my programm tries to connect to MySQL.localhost server with LOGIN and PASSWORD that is described in Project/src/main/java/auction/shared/Const.java in #19 and #23         
        public static final String DB_LOGIN = "root";
        /**
         * The constant DB_PASS.
         */
        public static final String DB_PASS = "misha";
        /**

Please set here your actual mySQL LOGIN and PASS in roject/src/main/java/auction/shared/Const.java in #19 and #23


Possible issue - exceptions if you tries to send some email (While registration, BUYPRO, if you win auction)
This is possible because I use Gmail to send email, and Google sometimes blok sending via java.mail due to new devise signed in. But sometimes it works fine on new devises. 
## Database
Open MySQL WorkBench and import dump.sql fwom Project/sqlDump