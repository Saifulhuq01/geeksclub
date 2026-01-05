# GeeksClub
GeeksClub is an application that helps techies to share their knowledge and thoughts.

## Tech Stack

### Backend
* Java
* Spring Boot
* Spring Data JPA
* Spring Security
* PostgreSQL
* Flyway Migrations
* JUnit
* Testcontainers
* Docker

### Frontend
* Angular
* Tailwind CSS

## Prerequisites
* JDK 25
* Node.js 24
* Angular CLI
* Docker and Docker Compose
* Your favourite IDE (Recommended: [IntelliJ IDEA](https://www.jetbrains.com/idea/))

Install JDK, Maven, etc using [SDKMAN](https://sdkman.io/)

```shell
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
$ sdk install java 25-tem
$ sdk install maven
```

Task is a task runner that we can use to run any arbitrary commands in easier way.

```shell
$ brew install go-task
(or)
$ go install github.com/go-task/task/v3/cmd/task@latest
```

## Using `task` to perform various tasks:

```shell
# Build backend and run tests
$ task build_backend

# Build frontend
$ task build_frontend

# Run application in docker container
$ task start
$ task stop
$ task restart
```