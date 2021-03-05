# Lottery Service

[![Build Status](https://travis-ci.org/mercanil/lottery-service.svg?branch=main)](https://travis-ci.org/mercanil/lottery-service)
[![codecov](https://codecov.io/gh/mercanil/lottery-service/branch/main/graph/badge.svg?token=M26LGLFH1E)](https://codecov.io/gh/mercanil/lottery-service)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=mercanil_lottery-service&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=mercanil_lottery-service)


REST interface to a simple lottery system
RULES
- If the sum of the values on a line is 2, the result for that line is 10.
- If they are all the same, the result is 5.
- So long as both 2nd and 3rd numbers are different from the 1st, the result is 1.
- Otherwise, the result is 0.

## Table of Contents

- [API](#API)
- [Requirements](#Requirements)
- [Getting Started](#Getting-Started)
- [Road Map](#Road-Map)
- [Credits](#Credits)
## API
![Alt text](./swagger/swagger-ss.png?raw=true "Optional Title")

swagger.json: `http://127.0.0.1:8080/api-docs`

swagger-ui: `http://127.0.0.1:8080/swagger-ui.html`

## Requirements
* [Git](https://git-scm.com/downloads)
* [JDK 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/download.cgi?Preferred=ftp://mirror.reverse.net/pub/apache/)

## Getting Started
- `mvn spring-boot:run`

*Congratulations!* Now you are using *Lottery-Service*

## Road Map

If you want to see a new feature feel free to [create a new Issue](https://github.com/mercanil/lottery-service/issues/new)

## Credits

Lottery-Service is created and maintained by Anil Mercan

* I am open to suggestions, feel free to email mercanil@gmail.com
* Pull requests are also welcome!

