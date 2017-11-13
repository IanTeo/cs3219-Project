# CIR

Link to Backend Repository: [https://github.com/IanTeo/cs3219-Project](https://github.com/IanTeo/cs3219-Project)<br>
Link to Frontend Repository: [https://github.com/AngShiYa/cs3219-d3](https://github.com/AngShiYa/cs3219-d3)

Name | Matriculation Number | Feature Implemented
----|----|----|
Ang Shi Ya | A0138601M | **Frontend Project Structure**, **UI/UX**, Visualization of **Time Series** & **Composition** & **Comparison** & **Relationship** Graphs
Ian Teo | A0139930B | **Json Data Parsing**, Visualization of **Text Analysis** Graph, **Server** Set up for Frontend and Backend, **Backend Project Structure**, **Top** Command, **Web** Command, **Word** Command and all related **Tests**
Yong Zhi Yuan | A0139655U | **Trend** Command, **Utility**, **Filters**, **Model** and all related **Tests**

## 1. Introduction

CIR is a website for the NLP Research Lab to visualize conference publication data using various queries. The website focus is solely on the visualization, and an additional `REST Server` is used to parse the data into a suitable format for visualization by the website.

## 2. Requirement Specification

## 3. Design and Implementation

### Architecture

We use a 3 tier architecture, which comprises of the following parts:

* `REST Server` (Java)
* `Website` (Javascript)
* `Resource` (JSON).

The `REST Server` preprocesses the `Resource`, which is the first 200,000 lines of data from  [http://labs.semanticscholar.org/corpus/](http://labs.semanticscholar.org/corpus/) and provides a service which answers queries with a JSON file suitable for visual representation. The `Website` queries the `REST Server` and visualizes the data it recieves.

<p align="center">
<img src="docs/architecture.png" width="550"><br>

<em>Figure 1: Architecture Overview Diagram</em>
</p>

This makes each of our layers independent, allowing us to work simulatanuously on different parts of the project at the same time, with minimal affect to the other parts of the system. The independence also makes unit testing each component easier as they are less coupled. Lastly, this architecture provides ease of maintaince, as changes in 1 layer will rarely affect other layers.

`Website` is created using node.js and d3.js. `REST Server` is created using Java. `Resources` is a file based database.

### Typical Flow of Application

<p align="center">
<img src="docs/typical_sequence.png" width="550"><br>

<em>Figure 2: Sequence Diagram of a Typical Flow in the Application</em>
</p>

At the start, `REST Server` has to prepare the data, which it requests from `Resources`. After parsing the data, the server will be open to HTTP GET requests.

User will query `Website`, which sends a HTTP GET request with the appropriate parameters to `REST Server`, which handles the request and passes back a suitable JSON representation which best suits the user's query, which `Website` will use to generate a suitable chart.

<p align="center">
<img src="docs/rest_server_architecture.png" width="800"><br>

<em>Figure 3: Architecture of REST Server</em>
</p>

`REST Server` is mainly comprised of 3 components, `Model`, `View` and `Logic`. `Model` is a data structure to store and represent the data. `View` is the way to communicate with external channels, in this case using HTTP, but can easily be changed for another type of view. `Logic` is where the main processing of the data happens. Command Pattern is used to encapsulate the different commands, making it easier to extend, maintain and add new commands. `Logic` also contains other `Utility`, such as **Filter** and **MapUtility** that provide commonly used features to manupilate data for different Commands.

### Implementation of RESTful Service

We chose not to use `Spring`, even though provides an easy way to create a RESTful Service on Java, for 2 reasons:

1. The size of the dependencies was bigger than the project. We only needed the basic RESTful functionalities, which would bloat our project with many files that we do not need.
2. Developers do not need to learn an additional framework to maintain/improve the current code base.

Instead, we opted to use `HttpServer`, which was included in `Java 6`.

### Continuous Integration

We use `JUnit` tests to perform automated tests application with `Gradle`, together with `JaCoCo` to generate the test coverage report.

<p align="center">
<img src="docs/jacoco_test_results.png" width="800"><br>

<em>Figure 4: Latest Test Code Coverage Results</em>
</p>

In addition, we use the static analysis tool `FindBugs`, to help reduce complexity and find common bugs.

These tools help to ensure that the application is always in a state that is ready to be deployed at any time. All these tools are run automatically by `Travis` whenever new code is pushed, except for `JaCoCo`, which cannot be run for free on a private repository.

## 4. Visualizations

### Time Series

### Composition

### Comparison

### Relationship

### Text Analysis


## 5. Additional Information

### Server
Initially, we intended to use `Heroku` to host both the website and the RESTful service, as it was free. However for Java, `Heroku` only catered towards deployment using `Spring` and `Ratpack`, and it was difficult to get the RESTful service using the built in `HttpServer` to work. In addition, the data file (500mb) was too big to be uploaded onto the **free** version.


#### RESTful Service
Eventually, we decided to use `DigitalOcean` to host the server. The steps to prepare the server to deploy the RESTful service are as follows:

1. ssh into the server
2. Create a `fat JAR`
3. Upload the data file and `fat JAR` file
4. Create a `service` in `/etc/systemd/system` to run the JAR file.
5. Run the `service`

javaserver.service 

```
[Unit]
Description=Java Server

[Service]
Type=simple
WorkingDirectory=/root
ExecStart/usr/bin/java -jar -Xmx1500m CIR.jar
Environment="PORT-80"

[Install]
WantedBy=multi-user.target
```

For this project, all these steps are automated, and the project can be run by executing `javaserver.sh`, which pulls the changes from github and runs the `service`

javaserver.sh

```
cd ~/cs3219-Project
git checkout master
git pull
./gradlew shadowJar
mv build/libs/CIR.jar ../CIR.jar

systemctl daemon-reload
systemctl stop javaserver.service
systemctl start javaserver.service
systemctl status javaservice.service
```

The RESTful service can be used at [128.199.249.171](http://128.199.249.171/)


#### Website

For the website, `Heroku` provided easy deployment using `node`. We deployed the website following the steps on their online [tutorial](https://devcenter.heroku.com/articles/getting-started-with-nodejs#introduction).

The website can be viewed at [cir-group-7.herokuapp.com](http://cir-group-7.herokuapp.com/)