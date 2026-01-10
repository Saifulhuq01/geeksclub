# CLAUDE.md - Development Guidelines for GeeksClub

This document provides comprehensive guidelines for agentic coding assistants working on the GeeksClub project. 
It covers build commands, testing approaches, code style conventions, and development workflows.

## Table of Contents
- [Project Overview](#project-overview)
- [Build, Lint, and Test Commands](#build-lint-and-test-commands)
- [Development Workflows](#development-workflows)
- [Commit and Pull Request Guidelines](#commit-and-pull-request-guidelines)

## Project Overview

GeeksClub is a full-stack application built with:
- **Backend**: Java 25, Spring Boot 4.x, PostgreSQL, Flyway migrations
- **Frontend**: Angular 21, TypeScript, Tailwind CSS
- **Infrastructure**: Docker, Docker Compose, GitHub Actions CI/CD

## Build, Lint, and Test Commands

### Backend (Java/Spring Boot)

#### Full Build with Tests
```bash
# Using Maven wrapper (recommended)
./mvnw clean spotless:apply verify

# Using Task runner
task build_backend
```

#### Quick Build (Skip Tests)
```bash
# Build only
./mvnw clean compile

# Build Docker image
./mvnw spring-boot:build-image -DskipTests
```

#### Run Single Test
```bash
# Run specific test class
./mvnw test -Dtest=MessageControllerTests

# Run specific test method
./mvnw test -Dtest=MessageControllerTests#shouldCreateMessageSuccessfully

# Run tests with pattern
./mvnw test -Dtest="*Controller*"
```

#### Code Quality & Formatting
```bash
# Format code (Spotless)
./mvnw spotless:apply

# Check formatting
./mvnw spotless:check

# Run SonarQube analysis (requires token)
./mvnw sonar:sonar
```

#### Test Coverage
```bash
# Run tests with coverage
./mvnw verify

# View coverage report (after running verify)
open geeksclub-api/target/jacoco/test/jacoco/index.html
```

### Frontend (Angular/TypeScript)

#### Full Build with Tests
```bash
# Install dependencies
npm install

# Build production
npm run build

# Using Task runner
task build_frontend
```

#### Development Server
```bash
# Start dev server
npm start
# or
ng serve
```

#### Run Tests
```bash
# Run all tests once
npm test

# Run tests in watch mode
npm test -- --watch

# Run specific test file
npm test -- --run src/app/components/some.component.spec.ts

# Run tests with coverage
npm test -- --coverage
```

#### Code Quality & Formatting
```bash
# Format code (Prettier)
npx prettier --write .

# Check formatting
npx prettier --check .
```

### High-Level Commands (Task Runner)

```bash
# Build everything
task

# Start full application (builds images and runs containers)
task start

# Stop application
task stop

# Restart application
task restart
```

## Development Workflows

### Local Development Setup

1. **Prerequisites**: JDK 25, Node.js 24, Docker, Angular CLI
2. **Clone repository**
3. **Backend setup**:
   ```bash
   cd geeksclub-api
   ./mvnw clean compile
   ```
4. **Frontend setup**:
   ```bash
   cd geeksclub-angular
   npm install
   ```
5. **Database**: Use Docker Compose for PostgreSQL
6. **Run locally**: Use Task commands or individual service commands

### Adding New Features

1. **Backend**:
   - Create DTOs in `rest.dto` package
   - Add service methods in appropriate service class
   - Create controller endpoints with proper validation
   - Add tests in corresponding test class
   - Update database schema if needed (Flyway migration)

2. **Frontend**:
   - Create components using Angular CLI
   - Use signals for state management
   - Implement proper error handling
   - Add unit tests for components and services

## Commit and Pull Request Guidelines

### Commit Messages
- **Format**: `type(scope): description`
- **Types**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- **Examples**:
  - `feat(auth): add JWT token refresh functionality`
  - `fix(api): handle null pointer in message service`
  - `test(ui): add component tests for login form`

### Pull Requests
- **Branch naming**: `feature/description`, `fix/issue-number-description`
- **Title**: Clear, concise description of changes
- **Description**: Include what was changed and why
- **Testing**: Ensure all tests pass, coverage meets requirements
- **Code review**: Address all reviewer feedback

### CI/CD Pipeline
- **Backend**: Maven verify (build, test, coverage, formatting)
- **Frontend**: npm build and test
- **Docker**: Build and publish images for main/release branches
- **Quality Gates**: SonarQube analysis for main branch

---

## Quick Reference

### Most Common Commands
```bash
# Backend
./mvnw clean spotless:apply verify                    # Full build with tests
./mvnw test -Dtest=SomeTestClass                      # Run specific test
./mvnw spotless:apply                                 # Format code

# Frontend  
npm install && npm run build                          # Build frontend
npm test                                             # Run tests
npx prettier --write .                               # Format code

# Full stack
task start                                           # Start everything
task build_backend && task build_frontend            # Build all
```

### Code Style Checklist
- [ ] Java: Spotless formatting applied
- [ ] TypeScript: Prettier formatting applied  
- [ ] Tests: Coverage ≥ 75% (backend)
- [ ] No console.log statements in production code
- [ ] Proper error handling implemented
- [ ] Security: No secrets committed, proper validation</content>
