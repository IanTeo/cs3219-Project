# CIR

Link to Backend Repository: [https://github.com/IanTeo/cs3219-Project](https://github.com/IanTeo/cs3219-Project)<br>
Link to Frontend Repository: [https://github.com/AngShiYa/cs3219-d3](https://github.com/AngShiYa/cs3219-d3)

Name | Matriculation Number | Feature Implemented
----|----|----|
Ang Shi Ya | A0138601M | **Frontend Project Structure**, **UI/UX**, Visualization of **Time Series** & **Composition** & **Comparison** & **Relationship** Graphs
Ian Teo | A0139930B | **Json Data Parsing**, Visualization of **Text Analysis** Graph, **Server** Set up for Frontend and Backend, **Backend Project Structure**, **Top** Command, **Web** Command, **Word** Command and all related **Tests**
Yong Zhi Yuan | A0139655U | **Trend** Command, **Utility**, **Filters**, **Model** and all related **Tests**

## 1. Introduction

This project comprises of 2 parts, a `RESTful Service` (Java) and `Website` (Javascript). The `RESTful Service` preprocesses the first 200,000 lines of data from  [http://labs.semanticscholar.org/corpus/](http://labs.semanticscholar.org/corpus/) and provides a service which answers queries with a JSON file suitable for visual representation. The `Website` queries the `RESTful Service` and visualizes the data it recieves.

## 2. Requirement Specification

## 3. Design and Implementation

### Architecture

We decided to use a 3 tier architecture, so that we could seperate the **view** (Presentation Layer), **logic** (Application Layer) and **model** (Data Layer) into 3 distinct components.

<p align="center">
<img src="docs/architecture.png" width="550"><br>

<em>Figure 1: Architecture Overview Diagram</em>
</p>

This makes each of our layers independent, allowing us to work simultaneously on different parts of the project at the same time, with minimal affect to the other parts of the system. The independence also makes unit testing each component easier as they are less coupled. Lastly, this architecture provides ease of maintenance, as changes in 1 layer will rarely affect other layers.

#### Logic

We decided to apply the `Command Pattern` as we have multiple Commands, and the executor of these Commands to not need to know anything about the command that it is executing.

<p align="center">
<img src="docs/command pattern.png" width="800"><br>
<em>Figure 2: Command Pattern Diagram</em>
</p>

This also follows the `Open-Closed Principle` as new Commands can be added without having to modify the existing Commands.

We created a `ParseException` to signify that there is an error with the parsing of the given data. `XCommandParser` will throw `ParseException` whenever compulsory fields are missing, or when any of the fields fail input validation. The erroneous fields will be captured as part of the error message, which allows the user to know which field to correct.

#### Trend Command

Our customer Simon mentioned that the trend he is looking for is one-dimensional, that is if the `year` is fixed, then the viewpoints for inspection is `conferences` (i.e. The comparisons made are between `conferences` for the same year). Conversely, if the `conference` is fixed, then the viewpoints for inspection is `years`. However, we thought that these graph plots are very limiting and do not convey much information. As such, we have decided to do a two-dimensional visualisation, that is both `years` and `conferences` can have varying values. This allows users to perform more meaningful comparisons, namely comparing different `conferences` across different `years`. 

Also, we have included a filtering functionality to allow users to filter data. What Simon required from us is to support mono-filtering (e.g. Only the term `Authors` will be filtered in the query: "Number of Papers written by Authors x, y, z in 2001"), however we have implemented the functionality to perform multiple filterings. As such, we can accept queries such as: "Number of Papers written by Authors x, y, z where Venue is ICSE or ACXiV". 

### Typical Flow of Application

<p align="center">
<img src="docs/sequence.png" width="550"><br>

<em>Figure 2: Sequence Diagram of a Typical Flow in the Application</em>
</p>

`View` receives a request from the user as a HTTP GET request, which it processes and sends a request over to `Logic`. `Logic` then determines the appropriate actions to take, and gets the relevant details from `Model`. After which, `Logic` processes the data into a JSON representation which best suits the user's query, which `View` displays to the user via HTTP.


### Implementation of RESTful Service

We chose not to use `Spring`, even though it provides an easy way to create a RESTful service on Java, for 2 reasons:

1. The size of the dependencies was bigger than the project. We only needed the basic RESTful functionalities, which would bloat our project with many files that we do not need.
2. Developers do not need to learn an additional framework to maintain/improve the current code base.

Instead, we opted to use `HttpServer`, which was included in `Java 6`.

### Continuous Integration

We use `JUnit` tests to perform automated tests application with `Gradle`, together with `JaCoCo` to generate the test coverage report.

<p align="center">
<img src="docs/jacoco_test_results.png" width="800"><br>

<em>Figure 3: Latest Test Code Coverage Results</em>
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