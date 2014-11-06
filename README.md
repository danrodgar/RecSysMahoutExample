RecSysMahoutExample
===================

Recommender system example using Apache Mahout

This example has been developed as a desktop Java application but it can be integrated (as a typical scenario) with Web servers (e.g., Apache HTTP) or Web application servers (Tomcat, Glassfish, etc.).

To install all necessary libraries Maven is used to download them automatically. It facilitaties the use of IDEs (Eclipse, Netbeans, etc.). In this example, datasets are stored in a SQL database (e.g. MySQL or PostgreSQL) and will be reached to generate the model but also the library can access to a CSV file, etc. In fact in this example, two flat files are generated from the database to be used by the recommender system algorithms (these files could be generated once a day or every few hours improve the performance).

Generic slides about recommender systems: http://www.cc.uah.es/drg/courses/datamining/IntroRecSys.pdf

Licence GNU GPL
