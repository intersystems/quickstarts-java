# quickstarts-java
This code shows how to connect a Java application to an InterSystems server using JDBC, XEP, Hibernate, the Native Java API, and multi-model programming. 

This repository is used in the [Java QuickStart](https://learning.intersystems.com/course/view.php?name=Java%20QS).

## Contents
* jdbcplaystocksTask7.java to see how to store and retrieve data relationally
* xepplaystocksTask6.java to see how to quickly store objects
* nativeplaystocksTask5.java to see how to run methods within InterSystems IRIS
* multiplayTask4.java to see multimodel access using JDBC, XEP, and Native API
* hibernateplaystocksTask6.java to see how to use a third-party tool to work with objects

## Configuration files
`src/Solutions/config.txt`: contains connection details for JDBC, XEP, Native API and multi-model.  
`src/hibernate.cfg.xml`: contains connections details, parameters and initial settings for Hibernate.

## How to use this sample

1.  Verify you have an [<span class="urlformat">InterSystems server</span>](https://learning.intersystems.com/course/view.php?name=Get%20InterSystems%20IRIS), and an IDE that supports Java, such as **Visual Studio Code**. 
2. Clone this repo and open it in your IDE.
3. Get the latest Java drivers from your InterSystems installation directory and place them in the lib folder. See InterSystems documentation full instructions on (getting the latest InterSystems Java drivers)[https://docs.intersystems.com/components/csp/docbook/DocBook.UI.Page.cls?KEY=ADRIVE#ADRIVE_jdbc].
4. Import and compile two class files within iris-load into the USER namespace on your InterSystems server
    do $system.OBJ.Load(path_"DemoStockCls.xml")
    compile
    do $system.OBJ.Load(path_"StocksUtil.xml")
    compile
5. Load data from iris-load into the USER namespace on your InterSystems server
    do ##class(Demo.Stock).LoadData(path_"all_stocks_1yr.csv")
6. Open `config.txt` file, located inside the **Solutions** package, and modify the `IP` and `password` to be the correct values for your InterSystems IRIS instance. Although `port` and `username` are most likely the defaults, you should verify those as well.

## How to use the Hibernate sample
1. Follow the instructions in InterSystems documentation for [setting up Hibernate drivers](https://docs.intersystems.com/iris20243/csp/docbook/DocBook.UI.Page.cls?KEY=BTPI_hibernate#BTPI_hibernate_install).

2. Due to its complexity, **Hibernate** has its own config file named `hibernate.cfg.xml`. Open `hibernate.cfg.xml` file, located inside the **src** folder, and modify the `IP` and `password` to be the correct values for your InterSystems IRIS instance. Althoug `port` and `username` are most likely the defaults, you should verify those as well.

You should now have several classes for **JDBC**, **XEP**, **Native API**, **multi-model** and **Hibernate** inside the **Solutions** package. 

Detailed instructions are included on the QuickStart page: https://learning.intersystems.com/course/view.php?name=Java%20QS