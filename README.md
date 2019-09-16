# quickstarts-java
This code shows JDBC, XEP, Native, multi-model, and Hibernate access. It is required for the Java QuickStart which can be found here: https://dev-gettingstarted.intersystems.com/language-quickstarts/java-quickstart/ 

## Contents
* jdbcplaystocksTask7.java to see how to store and retrieve data relationally
* xepplaystocksTask6.java to see how to quickly store objects
* nativeplaystocksTask5.java to see how to run methods within InterSystems IRIS
* multiplayTask4.java to see multi-model access using JDBC, XEP, and Native API
* hibernateplaystocksTask6.java to see how to use a third-party tool to work with objects

## Configuration files
`config.txt`: contains connection details for JDBC, XEP, Native API and multi-model.  
`hibernate.cfg.xml`: located in **src** folder, contains connections details, parameters and initial settings for Hibernate.

## How to Run

1.  Verify you have an [<span class="urlformat">instance of InterSystems IRIS</span>](https://learning.intersystems.com/course/view.php?name=Get%20InterSystems%20IRIS), and an IDE that supports Node.js (such as **Visual Studio Code**). If you are using AWS, Azure, or GCP, that you have followed the steps to [change the password for InterSystems IRIS](https://docs.intersystems.com/irislatest/csp/docbook/DocBook.UI.Page.cls?KEY=ACLOUD#ACLOUD_interact).
2. If you are using AWS, GCP, or Microsoft Azure, load the sample stock data into InterSystems IRIS:  
    `$ iris load http://github.com/intersystems/Samples-Stock-Data`  
If you are using InterSystems Labs, the sample stock data is already loaded. You can skip to the next step.
3. Clone the repo and open it in your IDE.
4. With Eclipse,
	* Select **File** > **Import** > **Git** > **Projects from Git**. Click **Next**.
	* Enter URI: https://github.com/intersystems/quickstarts-java
	* Select the **master** branch. Click **Next**.
	* Choose import existing project. Click **Next**.
	* Click **Finish**.
	* Open `config.txt` file, located inside the **Solutions** package, and modify the `IP` and `password` to be the correct values for your InterSystems IRIS instance. Although `port` and `username` are most likely the defaults, you should verify those as well.
	* Due to its complexity, **Hibernate** has its own config file named `hibernate.cfg.xml`. Open `hibernate.cfg.xml` file, located inside the **src** folder, and modify the `IP` and `password` to be the correct values for your InterSystems IRIS instance. Althoug `port` and `username` are most likely the defaults, you should verify those as well.

You should now have several classes for **JDBC**, **XEP**, **Native API**, **multi-model** and **Hibernate** inside the **Solutions** package. 

Detailed instructions are included on the QuickStart page: https://dev-gettingstarted.intersystems.com/language-quickstarts/java-quickstart/ 

