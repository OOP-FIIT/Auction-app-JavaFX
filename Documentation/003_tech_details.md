# Environment setup

Please ensure that you have:

* Maven  
* JDK 17
* JavaFX 13
* Scene Builder
* MySQL Connector/J 8.0.23

## Installation

---

### Maven dependencies

 Run  `$ cd Project` , then `$ mvn dependency:purge-local-repository`

---

### MySQL

Make sure MySQL is installed properly and running at port `3306`

Change ***DB_LOGIN*** and ***DB_PASS*** in `Project/src/main/java/auction/shared/Const.java`  
in lines   ***#19***  and ***#23***  

> ***#19***|  public static final String DB_LOGIN = " ENTER YOUR MySQL LOGIN ";  
>
> ***#23***|  public static final String DB_PASS = " ENTER YOUR MySQL PASSWORD ";
---

### Gmail

Use gmail account to send emails from company owner  
Change ***MAIL.LOGIN*** and ***MAIL.PASSWORD*** in `Project/src/main/java/auction/shared/Const.java`  
in lines   ***#163***  and ***#167***  

> ***#163***| public static final String LOGIN = "ENTER YOUR GMAIL LOGIN";
>
> ***#167***| public static final String PASSWORD = "ENTER YOUR GMAIL PASSWORD";

---

## Possible issue  

* Gmail may not send Emails via programm even if you configured login and password cause is is *vulnerable*

## Database

Open MySQL WorkBench and import dump.sql fwom Project/sqlDump