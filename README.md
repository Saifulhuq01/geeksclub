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

## How to run the application locally?

Run the Backend by starting the GeeksClubApplication from IDE or using the following command: 

```shell
$ cd geeksclub-api
$ mvn spring-boot:run //uses Docker Compose
$ mvn spring-boot:test-run //uses Testcontainers
```

Run the Frontend by starting the Angular application using the following command:

```shell
$ cd geeksclub-angular
$ npm install
$ ng serve
```

Now you can access the application at http://localhost:4200/

### Using `task` to perform various tasks:

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

## Contributing

- Fork the repo
- Create a GitHub issue for the bug or feature request 
- Create your feature branch (git checkout -b feature/foo)
- Commit your changes (git commit -am 'Add some foo')
- Push to the branch (git push origin feature/foo)
- Create a new Pull Request

It would be better to create an issue first to discuss the change you wish to make.

- Ensure that the code follows the project's coding standards and conventions
- Make sure to add tests for your changes.
- Test your changes thoroughly to ensure they work as expected
- Address any feedback or comments from reviewers
