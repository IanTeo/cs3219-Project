# CIR

Link to Backend:

* Repository: [https://github.com/IanTeo/cs3219-Project](https://github.com/IanTeo/cs3219-Project)
* Link: [http://128.199.249.171/](http://128.199.249.171/)

Link to Frontend:

* Repository: [https://github.com/AngShiYa/cs3219-d3](https://github.com/AngShiYa/cs3219-d3)
* Link: [http://cir-group-10.herokuapp.com/](http://cir-group-10.herokuapp.com/)


## 1. Introduction

**Ang Shi Ya (A0138601M)**<br>
Features Implemented:

* Frontend `Website` Project Structure
* UI/UX
* Responsive UI
* Visualization of **4.1 Time Series**
* Visualization of **4.2 Composition**
* Visualization of **4.3 Comparison**
* Visualization of **4.4 Relationship**

**Ian Teo (A0139930B)**<br>
Features Implemented:

* Backend `REST Server` Project Structure
* JSON data parsing
* Top Command (Data for **4.3 Comparison**)
* Web Command (Data for **4.4 Relationship**)
* Word Command (Data for **4.5 Text Analysis**)
* All related Tests for Backend
* Visualization of **4.5 Text Analysis**
* Server set up for Frontend and Backend
* `FindBugs`, `JaCoCo` and `Travis CI` set up

**Yong Zhi Yuan (A0139655U)**<br>
Features Implemented:

* Trend Command (Data for **4.1 Time Series**, **4.2 Composition**)
* Utility for `Logic`
* Filter for `Logic`
* `Model`
* All related Tests for Backend

Bonus features:

* Flexible Commands
* Responsive UI
* Beautiful and Intuitive UI
* Maintainable
* Extendable
* Cloud Integration

CIR is a website for the NLP Research Lab to visualize conference publication data using various queries. The `Website` focus is solely on the visualization, and an additional `REST Server` is used to parse the data into a suitable format for visualization by the website.

## 2. Requirement Specification

### 2.1 Functional Requirements

As a User, I can.. | so that I can..
----|----|
visualize trends over a time period with specific venues/authors/papers | compare the trends of the data that I am interested in
compare specific venues/authors/papers over a specific year | compare the trends of the data that I am interested in
view the top few venues/authors/papers | see what venues/authors/papers are of interest
filter information that I am not interested in |  remove confounders in the data
see more than 3 visualizations of the data | see the data from different angles

### 2.2 Non-Functional Requirements

* Should work on any platform/browser with internet connection
* Should come with automated tests that cover at least 90% of code base
* Should be easy to deploy (less than 3 steps to deploy Frontend/Backend application)

## 3. Design and Implementation

### 3.1 Architecture

We use a 3 tier architecture, which comprises of the following parts:

* `REST Server` (Java)
* `Website` (Javascript, d3.js, node.js)
* `Resource` (JSON file storage).

The `REST Server` preprocesses the `Resource`, which is the first 200,000 lines of data from  [http://labs.semanticscholar.org/corpus/](http://labs.semanticscholar.org/corpus/) and provides a service which answers queries with a JSON file suitable for visual representation. The `Website` queries the `REST Server` and visualizes the data it recieves.

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

### 3.2 Typical Flow of Application

<p align="center">
<img src="docs/typical_sequence.png" width="550"><br>

<em>Figure 2: Sequence Diagram of a Typical Flow in the Application</em>
</p>


At the start, `REST Server` has to prepare the data, which it requests from `Resources`. After parsing the data, the server will be open to HTTP GET requests.

User will query `Website`, which sends a HTTP GET request with the appropriate parameters to `REST Server`, which handles the request and passes back a suitable JSON representation which best suits the user's query, which `Website` will use to generate a suitable chart.

### 3.3 REST Server Architecture

<p align="center">
<img src="docs/rest_server_architecture.png" width="800"><br>

<em>Figure 3: Architecture of REST Server</em>
</p>

`REST Server` is mainly comprised of 3 components, `Model`, `View` and `Logic`. `Model` is a data structure to store and represent the data. `View` is the way to communicate with external channels, in this case using HTTP, but can easily be changed for another type of view. `Logic` is where the main processing of the data happens. **Command Pattern** is used to encapsulate the different commands, making it easier to extend, maintain and add new commands. `Logic` also contains other packages, such as **Filter**, **JsonConverter** and **MapUtility** that provide commonly used features to manupilate data for different Commands.

### 3.5 Typical flow of Logic Component

<p align="center">
<img src="docs/command_sequence.png" width="900"><br>

<em>Figure 4: Sequence Diagram of Trend Command</em>
</p>

When `Controller` recieves a request, it passes the request to `CommandParser` to choose the appropriate command. In this case, it chose trend command, and activates `TrendCommandParser` to parse the data and create a new `TrendCommand` object. Controller the executes the command, which in this case, gets the data from `Model`, and uses `Filter` to remove unwanted data. Once `TrendCommand` is done executing the command, it returns 

### 3.6 Implementation of RESTful Service

We chose not to use `Spring`, even though it provides an easy way to create a RESTful service on Java, for 2 reasons:


1. The size of the dependencies was bigger than the project. We only needed the basic RESTful functionalities, which would bloat our project with many files that we do not need.
2. Developers do not need to learn an additional framework to maintain/improve the current code base.

Instead, we opted to use `HttpServer`, which was included in `Java 6`.

`HttpServer` creates a listener on the specified port, based on the system's environment variable. If not port is specified, it defaults to port 8000. A single listener is created that acts as a **Front Controller** for the application. The listener waits for HTTP requests, parses the request and sends it to `Logic` to execute the request. Front Controller Pattern was chosen to avoid code duplication, as the parsing is similar for all requests.

### 3.7 Continuous Integration

We use `JUnit` tests to perform automated tests application with `Gradle`, together with `JaCoCo` to generate the test coverage report. In addition, we use the static analysis tool `FindBugs`, to help maintain a consistent level of code quality, reduce complexity and find common bugs and errors.

<p align="center">
<img src="docs/jacoco_test_results.png" width="800"><br>

<em>Figure 5: Latest Test Code Coverage Results</em>
</p>

These tools help to ensure that the application is always in a state that is ready to be deployed at any time. All these tools are run automatically by `Travis` whenever new code is pushed, except for `JaCoCo`, which cannot be run for free on a private repository. We added `JaCoCo` on the deadline of the assignment, when we made the repository public.

### 3.8 Error Handling

We added a Parser for each Command, seperate the parsing and validation logic from the actual execution logic. This allowed us to make robust error handling mechanisms without cluttering the execution logic of each command.

Errors are detected by the individual parsers and sent back as an `InvalidCommand`, where it sends a JSON representation of the error to `View`. Once `Website` recieves the error message, the user will be prompted with an appropriate error message, guiding the user to fix the problem area.

In addition, `Website` provides intuitive inputs like dropdown list for predefined categories, which minimizes erroneous input.

## 4. Visualizations

Visualization queries are performed through form based inputs, where the user either types the input or selects from a dropdown list. There are 2 types of dropdown lists:

* Category
  * **total**: Group **all** results in a single group
  * **paper**: Group the result by **paper titles**
  * **author**: Group the result by **author names**
  * **venue**: Group the result by **venues**
* Measure
  * **paper**: Count the sum of **papers** in the group
  * **author**: Count the sum of **authors** in the group
  * **venue**: Count the sum of **venues** in the group
  * **incitation**: Count the sum of **in-citations** (papers that cite this paper) in the group
  * **outcitation**: Count the sum of **out-citations** (papers that this paper cites) in the group

There are also 3 filters that can be applied to all trend based queries. The filters are:

* **Paper**: Only papers with titles specified here will be considered for the query
* **author**: Only papers with authors specified here will be considered for the query
* **venue**: Only papers with venues specified here will be considered for the query

The filters are optional, except for **4.1 Time Series** and **4.2 Composition**, where the filter for the selected category must be specified. This is because there can be a huge amount of data sent when the filter is not specified, which is too much for the small bandwidth of our free servers.

### 4.1 Time Series

<p align="center">
<img src="docs/series_visual.png" width="850"><br>

<em>Figure 6: Time Series Visualization</em>
</p>

This chart shows the **transition over time** for any specified venues/authors/papers. Here, we count the **number of papers** per year, group by **venues (ICIP, Lancet, Neuroreport, NeuroImage)** over the years **1997 to 2016**.

<p align="center">
<img src="docs/series_visual_mouse.png" width="600"><br>

<em>Figure 7: Mouse Over Lines</em>
</p>

To get a clearer view of the number of papers for each venue for a particular year, we can mouse over the chart to see the details.

<p align="center">
<img src="docs/series_visual_after.png" width="600"><br>

<em>Figure 8: Toggling Visibility of Lines</em>
</p>

The legend can be clicked to toggle visibility of the line with the clicked color, so that a better comparison can be made for the data of interest.

### 4.2 Composition

<p align="center">
<img src="docs/composition_visual.png" width="900"><br>

<em>Figure 9: Composition Visualization</em>
</p>

This chart shows the **contemporary comparison** for any specified venues/authors/papers. Previously, in Figure 5, there was a spike in number of papers for the venue **NeuroImage**. We can view that point of interest in more detail here. Here, we have the same fields, except we fix the year to **2016**.

<p align="center">
<img src="docs/composition_visual_mouse.png" width="600"><br>

<em>Figure 10: Mouse Over on a Slice</em>
</p>

You can **mouse over** any of the slices in the pie chart to see the exact count and percentage of the slice.

<p align="center">
<img src="docs/composition_visual_click.png" width="600"><br>

<em>Figure 11: Mouse Click on Multiple Slices</em>
</p>

You can also click on each slice if you want to see the total count of multiple slices.

### 4.3 Comparison

<p align="center">
<img src="docs/comparison_visual.png" width="900"><br>

<em>Figure 12: Comparison Visualization</em>
</p>

This chart shows the **Top N X of Y** for any specified venues/authors/papers. Here, we want to see the top 5 papers based on in-citation.

### 4.4 Relationship

<p align="center">
<img src="docs/relationship_visual.png" width="600"><br>

<em>Figure 13: Relationship Visualization</em>
</p>

This chart shows the citation relationship between papers. Previously, in Figure 11, we saw the top few papers based on in-citation. We can view one of the points of interest there using this query. Here, we are viewing the relationship network for the paper **Theory of Games and Economic Behavior**

<p align="center">
<img src="docs/relationship_visual_mouse.png" width="600"><br>

<em>Figure 14: Mouse Over Nodes</em>
</p>

We can mouse over any node to see more details about each individual paper in the relationship network

<p align="center">
<img src="docs/relationship_visual_after.png" width="600"><br>

<em>Figure 15: Moving the Year Slider</em>
</p>

The year slider can be changed to view the cumulative relationship network up to the selected year.

### 4.5 Text Analysis

<p align="center">
<img src="docs/text_visual.png" width="600"><br>

<em>Figure 16: Text Analysis Visualization</em>
</p>

This chart shows a simple text analysis of the specified category. Here, we can find out topics of interest for each of the different categories. Although Common stop words are already filtered by the `REST Server`, additional stop words can be added if the user feels the word found is not useful to the visualization. 

### Time Series Creation (Multi-line chart creation)

#### Purpose
The purpose of this visualization is to compare the overall trend for different categories across the years. Using this visualization, we are able to see how well a particular category is faring, relative to other competitor. This provides a broader view of the data.

<p align="center">
<img src="docs/series-plosVSarxiv.png" width="600"><br>

<em>Figure 10: Comparing the trend of publication for Plos one and Arxiv</em>
</p>

For example, figure 10 shows clearly that the number of papers from venue ArXiv has a increasing trend but at a slower rate than Plos one.

#### Steps

**1. Set up dimensions, margins and color of the chart**

Before we can start drawing the chart, we would need to specify the dimensions, margins and color domain of the chart.

```javascript
// set the dimensions and margins of the graph
var margin = {top: 20, right: 100, bottom: 30, left: 50},
  width = 960 - margin.left - margin.right,
  height = 500 - margin.top - margin.bottom;

// set the color of the graph
var color = d3.scaleOrdinal(d3.schemeCategory10);
``` 

**2. Use the specified dimensions and margins to set up the svg and range**

Using the specified dimensions and margins, we need to append a svg to our div so that the other components of the chart can be drawn on top of the svg. We also need to specify the range of the chart based on the dimensions.

```javascript
// append svg object to the div
// append a group element to svg
// moves the group element to the top left margin
var svg = d3.select("#plotarea")
  .append("svg")
  .attr("width", width + margin.left + margin.right)
  .attr("height", height + margin.top + margin.bottom)
  .append("g")
  .attr("transform", "translate(" + margin.left + ", " + margin.top + ")");
  
// set the range
var x = d3.scaleLinear().range([0, width]);
var y = d3.scaleLinear().range([height, 0]);
```

**3. Get the data**

Before we can draw the chart, we need to get the data. Any codes that required the data will be enclosed in the callback.

```javascript
d3.json(url, function(error, data) {
  // codes to draw the chart
}
```

**4. Set the x and y scale using the data**

```javascript
// scale the range of the data
minX = d3.min(data, function(kv) { return d3.min(kv.data, function(d) { return d.year; })});
maxX = d3.max(data, function(kv) { return d3.max(kv.data, function(d) { return d.year; })});
minY = d3.min(data, function(kv) { return d3.min(kv.data, function(d) { return d.count; })});
maxY = d3.max(data, function(kv) { return d3.max(kv.data, function(d) { return d.count; })});

x.domain([minX, maxX]);
y.domain([0, maxY]);
```

**5. Set the x and y axis**

```javascript
// set the axes
xAxis = d3.axisBottom(x).ticks(maxX - minX).tickFormat(d3.format("d"));
yAxis = d3.axisLeft(y).ticks(Math.min(10, maxY));
```

**6. Set the x and y values for the line**

```javascript
var line = d3.line()
  .x(function(d) { return x(d.year); })
  .y(function(d) { return y(d.count); });
```

**7. Draw the lines based on the data**

This set of codes follows the enter, update and exit data join concept of d3. This will be useful for the dynamic filtering of the data for our chart interaction.

```javascript
var category = svg.selectAll(".category")
  .data(data, function(d) { return d.series; });

  category.exit().remove();

  category = category.enter().append("g")
      .attr("class", "category");

  category.append("path")
    .attr("class", "line")
    .attr("d", function(d) { return line(d.data); })
    .style("fill", "none")
    .style("stroke", function(d) { return color(d.series); })
    .style("stroke-width", "2");
```

we will be enclosing the entire codes from step 4 till here in a method because it will be reused. Remember to call `update(data)` in place of the extracted codes.

```javascript
function update(data) {
  // the codes from step 4 to step 7
}
```

**8. Draw the axes**

As there is no concept of z-index for svg, we will add the axes only after we have drawn the data lines. At this point, a full line chart with axes should be drawn. We will continue to add additional functions to the chart to make it more interactive.

```javascript
// Add x axis
svg.append("g")
  .attr("class", "x axis")
  .attr("transform", "translate(0," + height + ")")
  .call(xAxis);

// Add y axis
svg.append("g")
  .attr("class", "y axis")
  .call(yAxis);
```

**9. Add the legend and interactive filtering function**

The following set of codes uses html checkbox as legend instead of svg rect to facilitate filtering of data lines on the line chart.

```javascript
var legend = d3.select("#dataseries").selectAll(".series")
  .data(data)
  .enter()
  .append("div")
  .attr("class", "series")

var checkbox = legend.append("div")
  .attr("class", "checkbox")
       
checkbox.append("input")
  .attr("class", "selection")
  .attr("checked", true)
  .attr("type", "checkbox")
  .attr("value", function(d) { return d.series; })
  .attr("id", function(d) { return d.series; })
  .on("change", selectionChanged)

checkbox.append("label")
   .attr("for", function(d) { return d.series; })
   .style("background", function(d) { return color(d.series); })

legend.append("text")
   .text(function(d) { return d.series; });
```

Now, we need to add the codes to handle the checkbox selections. After getting the checkbok selections store as an array, we will filter the data by matching it with the selections array. Once we get the filtered data, we will pass it through update() to update the chart.

```javascript
function selectionChanged() {
  var selections = [];
  d3.selectAll(".selection").each(function(d) {
    var label = d3.select(this.parentNode).select("label")
    var checkbox = d3.select(this);
    
    if (checkbox.property("checked")) {
      selections.push(checkbox.property("value"));
      label.style("background", function(d) { return color(d.series); })
    } else {
       label.style("background", function(d) { return "#ddd"; })
    }
  });
  
  filteredData = data.filter(function(d) { return selections.indexOf(d.series) > -1 });
  update(filteredData);      
}
```

At this point, the legend should consists of normal checkboxes with ticks. To make the checkboxes looks more like a legend, we would need to add styling to the checkboxes. The follwing css codes makes the checkboxes looks circle, and instead of a tick, the checkbox will be filled with the respective data line color when it is selected.

```css
input[type=checkbox] {
  visibility: hidden;
}
      
.checkbox {
  position: relative;
  float: left;
  width: 20px;
  height: 20px;
  background: #ddd;
  border-radius: 100%;
}

.checkbox label {
  position: absolute;
  display: block;
  width: 16px;
  height: 16px;
  border-radius: 100%;
  transition: all .3s ease;
  cursor: pointer;
  top: 2px;
  left: 2px;
  z-index: 1;
}
``` 

**10. Scale the axis based on filter data**

This step is added adjust the axis when we add or remove data lines. As we are only filter the data lines, we will only be adjusting the y axis.

```javascript
var t = d3.transition().duration(300)

svg.select(".y").transition(t).call(yAxis);
svg.selectAll(".line").transition(t).attr("d", function(d) { return line(d.data); });
```

**11. Add mouseover effect**

To enable mouseover effect, we must first create a 'g' element to house all the mouseover element (i.e. tooltip).

```javascript
var mouseG = svg.append("g")
  .attr("class", "mouse-over-effects");
```

Now, create a reference line that will follow the mouse as we mouseover the line chart.

```javascript
mouseG.append("path")
  .attr("class", "mouse-line")
  .style("stroke", "black")
  .style("stroke-width", "1px")
  .style("opacity", "0");
```

Next, we will add the circles that will appear on the data points of every line. To do that, we have to add the following codes in the update() method.

```javascript
var mousePerLine = mouseG.selectAll('.mouse-per-line')
  .data(data, function(d) { return d.series; });

mousePerLine.exit().remove();

mousePerLine = mousePerLine.enter().append("g")
  .attr("class", "mouse-per-line");

mousePerLine.append("circle")
  .attr("r", 7)
  .style("stroke", function(d) { return color(d.series); })
  .style("fill", "none")
  .style("stroke-width", "1px")
  .style("opacity", "0");
```

Next, we will add the tooltip that shows us the values of each data points. To do that, we have to add the following codes in the update() method too.

```javascript
mouseG.selectAll(".tooltip").remove();

var tooltip = mouseG.append("g")
  .attr("class", "tooltip")
  .style("opacity", "0")

var tooltipData = tooltip.selectAll(".tooltip-data")
  .data(data, function(d) { return d.series; })
  .enter().append("g")
  .attr("class", "tooltip-data")
  .attr("transform", function(d, i) {
    return "translate(0," + i * 20 + ")";
  })

tooltipData.append("circle")
  .attr("r", 5)
  .style("fill", function(d) { return color(d.series); })

tooltipData.append("text")
  .attr("class", "tooltip-data-text");
```

Now that have all the mouseover effect we want added to the chart, we now have to add the handler to set the effects where we want it to be. To do that, we need to add an svg overlay that listens for mouseover events.

```javascript
var overlay = mouseG.append('svg:rect')
  .attr('width', width)
  .attr('height', height)
  .attr('fill', 'none')
  .attr('pointer-events', 'all');
  
// mouseout handler
overlay.on('mouseout', function() {
  d3.select(".mouse-line").style("opacity", "0");
  d3.selectAll(".mouse-per-line circle").style("opacity", "0");
  d3.selectAll(".tooltip").style("opacity", "0");
});

// mouseover handler
overlay.on('mouseover', function() {
  d3.select(".mouse-line").style("opacity", "1");
  d3.selectAll(".mouse-per-line circle").style("opacity", "1");
  d3.selectAll(".tooltip").style("opacity", "1");
});

// mousemove handler
overlay.on("mousemove", function() {
  var mouse = d3.mouse(this);
  var xDate, bisect, d0, d1, point;
 
  d3.selectAll(".mouse-per-line")
    .attr("transform", function(d, i) {
    // calculate and choose the data point to snap to
    xDate = x.invert(mouse[0]);
    bisect = d3.bisector(function(d) { return d.year; }).right;
    idx = bisect(d.data, xDate);
    d0 = d.data[idx - 1];
    d1 = d.data[idx]l;
    point = xDate - d0.year > d1.year - xDate ? d1 : d0;  
    return "translate(" + x(point.year) + "," + y(point.count) +")";
  });

  // draw the reference mouse line
  d3.select(".mouse-line")
    .attr("d", function() {
      var d = "M" + x(point.year) + "," + height;
      d += " " + x(point.year) + "," + 0;
      return d;
    });

  // set the tooltip to follow the mouse line
  d3.select(".tooltip")
    .attr("transform", "translate(" + (x(point.year) + 20) + "," + height/2 +")");

  // position the data in the tooltip
  d3.selectAll(".tooltip-data-text")
    .attr("transform", "translate(" + 10 + "," + 5 +")")
    .text(function(d) {
      return d.data.filter(function(pair) { 
        return pair.year == point.year; 
      }).map(function(pair) { return pair.count; });
    });
  })
```

Viola! At the end of this, the created line chart should be capable of displaying multiple lines, filtering/toggling the visibility of the data lines by clicking the legend as well as support mouseover effects like the vertical reference line and tooltip to show the data.

## 5. Additional Information

### Server
Initially, we intended to use `Heroku` to host both the website and the RESTful service, as it was free. However for Java, `Heroku` only catered towards deployment using `Spring` and `Ratpack`, and it was difficult to get the RESTful service using the built in `HttpServer` to work. In addition, the data file (500mb) was too big to be uploaded onto the **free** version.


#### 5.1 RESTful Service
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

#### 5.2 Website

For the website, `Heroku` provided easy deployment using `node`. We deployed the website following the steps on their online [tutorial](https://devcenter.heroku.com/articles/getting-started-with-nodejs#introduction).
