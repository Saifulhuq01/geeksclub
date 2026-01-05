# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

GeeksClub is an application that helps techies to share their knowledge and thoughts.

## Build and Development Commands

### Using Maven Wrapper (Recommended)
- **Build and test**: `./mvnw clean verify`
- **Run application**: `./mvnw spring-boot:run`
- **Format code**: `./mvnw spotless:apply`
- **Build Docker image**: `./mvnw clean compile spring-boot:build-image -DskipTests`

### Using Task (Alternative)
The project includes a Taskfile.yml for common operations:
- **Test (includes formatting)**: `task test`
- **Format code**: `task format`
- **Build Docker image**: `task build_image`
- **Start with Docker Compose**: `task start`
- **Stop Docker Compose**: `task stop`

### Run Tests
- **All tests**: `./mvnw verify`
- **Single test class**: `./mvnw test -Dtest=UserControllerTests`
- **Single test method**: `./mvnw test -Dtest=UserControllerTests#shouldRegisterUser`

### Database
- PostgreSQL 18 is used for development and testing
- Spring Boot Docker Compose support automatically starts PostgreSQL when running locally
- Database migrations are managed by Flyway in `src/main/resources/db/migration/`

## Code Formatting

The project uses Spotless with Palantir Java Format:
- Formatting is enforced during the compile phase
- Run `./mvnw spotless:apply` to format code before committing
- CI will fail if code is not properly formatted

## Development Practices
- Format code using `./mvnw spotless:apply` before running tests
- Commit messages: follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)
