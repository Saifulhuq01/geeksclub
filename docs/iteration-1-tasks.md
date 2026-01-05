# GeeksClub - Iteration 0 Tasks

**Iteration**: Project Setup (Iteration 0)
**Goal**: Set up the project foundation, development environment, and CI/CD pipeline.

## Backend Tasks

### 1. Project Initialization

- [x] Create Spring Boot project with Maven
  - [x] Use Spring Initializr with Java 25 (or latest LTS)
  - [x] Add core dependencies (Web, Data JPA, Security, Validation)
  - [x] Set up initial project structure
- [x] Configure project structure
  - [x] Create package structure (controller, service, repository, model, dto, config, exception)
  - [x] Decide on module structure (users, messages, etc)
- [x] Set up application.propertie
  - [x] Configure server port
  - [x] Set up application name
  - [x] Configure logging levels
- [x] Configure Spring profiles (local, prod)
  - [x] Create application-local.propertie
  - [x] Create application-prod.propertie
  - [x] Set active profile configuration

### 2. Database Setup

- [x] Set up PostgreSQL locally using Docker Compose
  - [x] Add PostgreSQL service to docker-compose.yml
  - [x] Configure database name, username, password
  - [x] Set up volume for data persistence
  - [x] Configure port mapping (5432)
- [x] Configure Spring Data JPA
  - [x] Add Spring Data JPA dependency
  - [x] Add PostgreSQL driver dependency
  - [x] Configure datasource in application.propertie
  - [x] Set up JPA properties (ddl-auto, show-sql, etc.)
- [x] Set up Flyway for database migrations
  - [x] Add Flyway dependency
  - [x] Create db/migration folder structure
  - [x] Configure Flyway settings in application.propertie
- [x] Create initial migration scripts
  - [x] V1__create_users_table.sql
  - [x] V2__create_messages_table.sql
  - [x] V3__create_votes_table.sql
  - [x] V4__initialize_seed_data.sql

### 3. Security Configuration

- [x] Configure Spring Security
  - [x] Add Spring Security dependency
  - [x] Create SecurityConfig class
  - [x] Disable default security for initial setup
  - [x] Plan JWT authentication architecture
- [x] Set up JWT authentication infrastructure
  - [x] Add oauth2-resource-server library dependency
  - [x] Create JWT configuration properties
  - [x] Create JwtTokenProvider skeleton class
- [x] Create security filter chain
  - [x] Define public endpoints (auth, health check)
  - [x] Define protected endpoints
- [x] Configure CORS
  - [x] Set up CORS configuration
  - [x] Allow frontend origin (http://localhost:4200)
  - [x] Configure allowed methods and headers

### 4. Kafka Setup

- [x] Set up Kafka locally using Docker Compose
  - [x] Add Kafka service to docker-compose.yml
  - [x] Configure Kafka port (9092)
  - [x] Set up volumes for Kafka data
- [x] Configure Spring Kafka
  - [x] Add Spring Kafka dependency
  - [x] Configure Kafka bootstrap servers in application.properties
  - [x] Set up consumer and producer properties
- [x] Create Kafka configuration classes
  - [x] Create KafkaProducerConfig
  - [x] Create KafkaConsumerConfig
  - [x] Create KafkaTopicConfig
- [x] Set up topic creation
  - [x] Define topic names (user-events, message-events, vote-events)
  - [x] Create topics with appropriate partitions and replication
  - [x] Add topic creation beans

### 5. Testing Infrastructure

- [x] Configure Testcontainers for integration tests
  - [x] Add Testcontainers dependency
  - [x] Add Testcontainers PostgreSQL module
  - [x] Add Testcontainers Kafka module
- [x] Set up test database
  - [x] Configure test database container
  - [x] Set up test data cleanup strategy
- [x] Create base test classes
  - [x] Create BaseIntegrationTest
  - [x] Create test utilities and helpers
- [x] Configure test profiles
  - [x] Create application-test.properties
  - [x] Configure test logging levels
  - [x] Set up test-specific properties

## Frontend Tasks

### 1. Angular Project Setup

- [x] Create new Angular project with latest version
  - [x] Install Node.js and npm (verify versions)
  - [x] Install Angular CLI globally
  - [x] Generate new Angular project (`ng new geeksclub-ui`)
  - [x] Choose routing: Yes
  - [x] Choose stylesheet format: CSS/SCSS
- [x] Set up project structure
  - [x] Set up folder structure (components, services, models, guards, interceptors)
- [x] Configure Tailwind CSS
  - [x] Install Tailwind CSS and dependencies
  - [x] Initialize Tailwind config
  - [x] Configure tailwind.config.js
  - [x] Add Tailwind directives to styles.css
  - [x] Test Tailwind classes

### 2. Development Environment

- [x] Configure environment files
  - [x] Set up environment.ts with API URL
  - [x] Set up environment.prod.ts with production API URL
  - [x] Add environment-specific configurations
  - [x] Document environment variables

- [x] Configure Angular Router
  - [x] Configure router imports
  - [x] Add <router-outlet> to app component
- [x] Create routing config
  - [x] Define initial routes (home, login, register)
  - [x] Set up lazy loading for feature modules

## DevOps Tasks

### 1. Docker Configuration

- [x] Dockerize backend using Buildpacks support
- [x] Create Dockerfile for frontend
  - [x] Create multi-stage Dockerfile
  - [x] Add Node build stage
  - [x] Add nginx runtime stage
  - [x] Configure nginx.conf
  - [x] Test Docker build
- [x] Create docker-compose.yml for local development
  - [x] Add backend service
  - [x] Add frontend service
  - [x] Add PostgreSQL service
  - [x] Add Kafka service

### 2. CI/CD Pipeline

- [x] Set up GitHub Actions / GitLab CI
  - [x] Create .github/workflows directory
  - [x] Choose CI platform (GitHub Actions recommended)
  - [x] Set up repository secrets
- [x] Configure build pipeline
  - [x] Create workflow YAML file
  - [x] Add backend build job (Maven)
  - [x] Add frontend build job (npm)
- [x] Configure test execution
  - [x] Add backend test execution
  - [x] Add frontend test execution

### 3. Documentation

- [x] README.md with setup instructions
  - [x] Add project description
  - [x] Add prerequisites section
  - [x] Add installation instructions
  - [x] Add running instructions (backend, frontend, docker)
  - [x] Add testing instructions
  - [x] Add the tech stack list
