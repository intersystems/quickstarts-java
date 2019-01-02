# quickstarts-java
This code shows JDBC, XEP, Native, multimodel, and Hibernate access. It is required for the Java quickstart which can be found here: https://learning.intersystems.com/course/view.php?name=Java%20QS 

## Contents
* jdbcplaystocksTask7.java to see how to store and retrieve data relationally
* xepplaystocksTask6.java to see how to quickly store objects
* nativeplaystocksTask5.java to see how to run methods within InterSystems IRIS
* multiplayTask4.java to see multimodel access using JDBC, XEP, and Native API
* hibernateplaystocksTask6.java to see how to use a third-party tool to work with objects

## Configuration files
`config.txt`: contains connection details for JDBC, XEP, Native API and multi-model.
`hibernate.cfg.xml`: located in *src* folder, contains connections details, parameters and initial settings for Hibernate.

## How to Run
To run this code:

1. Visit [Direct Access to InterSystems IRIS](https://learning.intersystems.com/course/view.php?name=Java%20Build), 
[Microsoft Azure](https://azuremarketplace.microsoft.com/en-us/marketplace/apps/intersystems.intersystems-iris-single-node) or 
[Google Cloud Platform](https://console.cloud.google.com/marketplace/details/intersystems-launcher/intersystems-iris-community) 
marketplaces to get InterSystems IRIS instance.
2. If you use [Microsoft Azure](https://azuremarketplace.microsoft.com/en-us/marketplace/apps/intersystems.intersystems-iris-single-node) or 
[Google Cloud Platform](https://console.cloud.google.com/marketplace/details/intersystems-launcher/intersystems-iris-community), 
you need to [load data into your instance](https://github.com/intersystems/Samples-Stock-Data). 
3. Clone the repo
4. With Eclipse,
 
	* Select File > Import > Git > Projects from Git. Click Next.
	* Enter URI: https://github.com/intersystems/quickstarts-java
	* Select the master branch. Click Next.
	* Choose import existing project. Click Next.
	* Click Finish.
	* Open `config.txt` file, located inside the **Solutions** package, and modify the `IP` and `password` to be the correct values for your InterSystems IRIS instance. 
`Port` and `username` are most likely the defaults but you can verify those as well.
	* Due to its complexity, **Hibernate** has its own config file named `hibernate.cfg.xml`. Open `hibernate.cfg.xml` file, located inside the **src** folder, and modify the `IP` and `password` to be the correct values for your InterSystems IRIS instance. `Port` and `username` are most likely the defaults but you can verify those as well.

You should now have several classes for **JDBC**, **XEP**, **Native API**, **multi-model** and **Hibernate** inside the **Solutions** package. 

Detailed instructions are included on the QuickStart page: https://learning.intersystems.com/course/view.php?name=Java%20QS 

