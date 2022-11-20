# Software Engineering Assessment

**This is the Fastned software engineering assessment. The assessment is intended to give us insights into your technical abilities, engineering approach and general technical working habits. We view your performance on this assessment as indicative of the work you will deliver as a Fastned software engineer.**

The assessment consists of an assignment to prepare beforehand and a presentation about your implementation of the assignment at the Fastned’s office or through video conference.

We ask you to treat the assessment confidential so we can use it again in the future. Please send us your solution when you think it is ready.

At the assessment presentation, you are free to deliver your presentation in any form, but we expect you to cover:

* The overall approach you took to the assignment
* The architecture of the solution delivered
* Your solution for each of the user stories

---

## Assignment

### The Fastned Solar Charging Network

In an ongoing push to make the world greener, Fastned is investing in a large solar charging network to power their power stations. In the context of capacity planning, Fastned has the desire to build a web-service which enables them to simulate the output of the network over a given period of time.

The following facts define the basis of the (simplified) model:

* Each solar grid produces an optimal power output of 20*(1-(D/365*0,005))kW (D=age in days).
* Each year has 1550 hours of sun.
* Since NL is not a very sunny country, we can (only) expect around 2,5 kWh per m2
* To compensate days with a lower sun strength, we simplify our model to 1000 hours of “full sun”
* Power output can be calculated over the “full sun” hours with the expectation of optimal power output of solar panels during those “full sun” hours.
* Solar grids have to be installed first, so only start to produce energy after 60 days of placement (age 0 is day of placement).
* A solar panel breaks down after 25 years of usage.

### Assumptions

* The age of a grid is defined in days.
* When querying your app, the elapsed time in days (T) is used. A value of for instance 13 means that day 12 has elapsed and we’re at midnight day 13.

### Clarifications

The project owner is available at [erhan.gullu@fastned.nl](mailto:erhan.gullu@fastned.nl) if during the course of developing the application you have any questions or want clarification on the requirements.

## The Stories

### FAST-1: As a user, I want to be able to read in a JSON file that contains data about my network so that I can query it.

Input network.json:

```json
[
  {
    "name": "Amsterdam",
    "age": 854
  },
  {
    "name": "Groningen",
    "age": 473
  },
  {
    "name": "Maastricht",
    "age": 253
  }
]
```

Your program should take 2 parameters:

1. JSON file to read
2. An integer T, representing the elapsed time in days

**Output Format:**

```shell
Produced: [Output] kWh
Network:
	[NAME] in use for [AGE] days
	[NAME] in use for [AGE] days
```

### FAST-2: As a user, I want to be able to load my network into the application using an HTTP REST service.

I want to be able to run the application and load my current state of the network into it. Once I load a network, any previous state of my application should be reset to the initial state.

The following are the HTTP requests I want to make.

• **POST** /solar-simulator/load: Which returns a Http Status Code 205 (Reset Content)

**Samples:**

Request: POST /solar-simulator/load

```json
[
  {
    "name": "Amsterdam",
    "age": 854
  },
  {
    "name": "Groningen",
    "age": 473
  },
  {
    "name": "Maastricht",
    "age": 253
  }
]
```

Response: status = 205

### FAST-3: As a user, I want to be able to query my network and its total output over time using a HTTP REST services which output JSON data

The following are the requests you wish to make.

• **GET** /solar-simulator/output/T: Which returns a result of your total output at T days

• **GET** /solar-simulator/network/T: Which returns a result of your network at T days

**Samples:**

Request: GET /solar-simulator/output/13

Response:

```json
{ 
  "total-output-in-kwh" : [OUTPUT]
}
```

## Bonus

### FAST-4: As a user, I want to have a user interface in my browser which I can use to input information.

The user interface should be able to:

* Add and remove grids from our network of solar panels
* To calculate the total output of the solar network at a given day T or at a given year.

### FAST-5: As a product owner I want to be surprised by your ingenuity so that I can show off to the business to increase the budget for my simulation tool.

* You can do anything that we haven’t thought of, so that I can show off some cool stuff to our CEO!

## Deadline: ##

When you think **it’s ready**
