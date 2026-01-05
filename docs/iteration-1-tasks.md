# GeeksClub - Iteration 0 Tasks

**Iteration**: Project Setup (Iteration 0)
**Duration**: 3-4 days
**Goal**: Set up the project foundation, development environment, and CI/CD pipeline.

---

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

### 5. Spring AI Setup

- [ ] Add Spring AI dependencies
  - [ ] Add Spring AI core dependency
  - [ ] Add Spring AI OpenAI dependency (or chosen provider)
  - [ ] Check compatibility with Spring Boot version
- [ ] Configure AI model connection
  - [ ] Set up API key configuration (environment variable)
  - [ ] Configure AI model settings in application.properties
  - [ ] Add properties for model name, temperature, etc.
- [ ] Create basic spam detection service skeleton
  - [ ] Create SpamDetectionService interface
  - [ ] Create SpamDetectionServiceImpl with @Async
  - [ ] Add placeholder spam detection logic
  - [ ] Configure async executor

### 6. Testing Infrastructure

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

---

## Frontend Tasks

### 1. Angular Project Setup

- [ ] Create new Angular project with latest version
  - [ ] Install Node.js and npm (verify versions)
  - [ ] Install Angular CLI globally
  - [ ] Generate new Angular project (`ng new geeksclub-ui`)
  - [ ] Choose routing: Yes
  - [ ] Choose stylesheet format: CSS/SCSS
- [ ] Set up project structure
  - [ ] Create core module for singleton services
  - [ ] Create shared module for reusable components
  - [ ] Create feature modules structure
  - [ ] Set up folder structure (components, services, models, guards, interceptors)
- [ ] Configure Tailwind CSS
  - [ ] Install Tailwind CSS and dependencies
  - [ ] Initialize Tailwind config
  - [ ] Configure tailwind.config.js
  - [ ] Add Tailwind directives to styles.css
  - [ ] Test Tailwind classes

### 2. Development Environment

- [ ] Configure environment files
  - [ ] Set up environment.ts with API URL
  - [ ] Set up environment.prod.ts with production API URL
  - [ ] Add environment-specific configurations
  - [ ] Document environment variables
- [ ] Set up proxy configuration for API calls
  - [ ] Create proxy.conf.json
  - [ ] Configure proxy to backend (http://localhost:8080)
  - [ ] Update angular.json with proxy config
  - [ ] Test proxy configuration
- [ ] Configure TypeScript strict mode
  - [ ] Enable strict mode in tsconfig.json
  - [ ] Enable strictNullChecks
  - [ ] Enable strictFunctionTypes
  - [ ] Fix any resulting type errors
- [ ] Set up ESLint and Prettier
  - [ ] Install ESLint and Angular ESLint
  - [ ] Create .eslintrc.json configuration
  - [ ] Install Prettier
  - [ ] Create .prettierrc configuration
  - [ ] Add format and lint scripts to package.json
  - [ ] Configure VS Code settings (optional)

### 3. Core Services

- [ ] Create HTTP interceptor for JWT tokens
  - [ ] Generate AuthInterceptor
  - [ ] Add logic to attach JWT token to requests
  - [ ] Exclude auth endpoints from token attachment
  - [ ] Register interceptor in providers
- [ ] Create error handling service
  - [ ] Generate ErrorHandlerService
  - [ ] Create error handling logic
  - [ ] Add error logging
  - [ ] Create user-friendly error messages
- [ ] Create notification/toast service
  - [ ] Generate NotificationService
  - [ ] Choose notification library (or use PrimeNG Toast)
  - [ ] Create success/error/info/warning methods
  - [ ] Add notification component to app component
- [ ] Create loading indicator service
  - [ ] Generate LoadingService
  - [ ] Create loading state management
  - [ ] Create loading indicator component
  - [ ] Add loading indicator to app component

### 4. Routing Setup

- [ ] Configure Angular Router
  - [ ] Set up app-routing.module.ts
  - [ ] Configure router imports
  - [ ] Add <router-outlet> to app component
- [ ] Create routing module
  - [ ] Define initial routes (home, login, register)
  - [ ] Set up lazy loading for feature modules
  - [ ] Configure route preloading strategy
- [ ] Set up route guards skeleton
  - [ ] Generate AuthGuard skeleton
  - [ ] Generate AdminGuard skeleton
  - [ ] Add guard interfaces
  - [ ] Plan guard implementation for next iteration

---

## DevOps Tasks

### 1. Docker Configuration

- [ ] Create Dockerfile for backend
  - [ ] Create multi-stage Dockerfile
  - [ ] Add Maven build stage
  - [ ] Add runtime stage with JRE
  - [ ] Configure EXPOSE port
  - [ ] Add HEALTHCHECK instruction
  - [ ] Test Docker build
- [ ] Create Dockerfile for frontend
  - [ ] Create multi-stage Dockerfile
  - [ ] Add Node build stage
  - [ ] Add nginx runtime stage
  - [ ] Configure nginx.conf
  - [ ] Test Docker build
- [ ] Create docker-compose.yml for local development
  - [ ] Add backend service
  - [ ] Add frontend service
  - [ ] Add PostgreSQL service
  - [ ] Add Kafka service
  - [ ] Add depends_on relationships
- [ ] Set up Docker networks and volumes
  - [ ] Create custom network for services
  - [ ] Create volume for PostgreSQL data
  - [ ] Create volume for Kafka data
  - [ ] Test docker-compose up

### 2. CI/CD Pipeline

- [ ] Set up GitHub Actions / GitLab CI
  - [ ] Create .github/workflows directory
  - [ ] Choose CI platform (GitHub Actions recommended)
  - [ ] Set up repository secrets
- [ ] Configure build pipeline
  - [ ] Create workflow YAML file
  - [ ] Add backend build job (Maven)
  - [ ] Add frontend build job (npm)
  - [ ] Configure artifact storage
- [ ] Configure test execution
  - [ ] Add backend test execution
  - [ ] Add frontend test execution
  - [ ] Configure test result reporting
  - [ ] Set up test coverage reporting
- [ ] Set up code quality checks
  - [ ] Add linting checks (backend: Checkstyle)
  - [ ] Add linting checks (frontend: ESLint)
  - [ ] Optional: Configure SonarQube/CodeClimate
  - [ ] Add branch protection rules

### 3. Documentation

- [ ] README.md with setup instructions
  - [ ] Add project description
  - [ ] Add prerequisites section
  - [ ] Add installation instructions
  - [ ] Add running instructions (backend, frontend, docker)
  - [ ] Add testing instructions
  - [ ] Add troubleshooting section
  - [ ] Add technology stack list
- [ ] CONTRIBUTING.md guidelines
  - [ ] Add code of conduct
  - [ ] Add contribution guidelines
  - [ ] Add branch naming conventions
  - [ ] Add commit message conventions
  - [ ] Add pull request process
  - [ ] Add coding standards
- [ ] API documentation setup (Swagger/OpenAPI)
  - [ ] Add Springdoc OpenAPI dependency
  - [ ] Configure Swagger UI
  - [ ] Add OpenAPI annotations to controllers
  - [ ] Test Swagger UI (http://localhost:8080/swagger-ui.html)
  - [ ] Configure API documentation properties

---

## Validation & Testing Tasks

- [ ] Verify backend health check endpoint
  - [ ] Create HealthController
  - [ ] Implement GET /actuator/health endpoint
  - [ ] Test endpoint returns 200 OK
- [ ] Verify frontend landing page
  - [ ] Create basic home component
  - [ ] Add welcome message
  - [ ] Test page loads at http://localhost:4200
- [ ] Test Docker Compose setup
  - [ ] Run `docker-compose up`
  - [ ] Verify all services start
  - [ ] Verify backend connects to PostgreSQL
  - [ ] Verify backend connects to Kafka
  - [ ] Verify frontend proxies to backend
- [ ] Test database migrations
  - [ ] Run application and verify Flyway executes
  - [ ] Check all tables are created
  - [ ] Verify indexes are created
  - [ ] Verify triggers are created
- [ ] Run all tests
  - [ ] Run backend unit tests: `mvn test`
  - [ ] Run backend integration tests: `mvn verify`
  - [ ] Run frontend tests: `ng test`
  - [ ] Verify all tests pass
- [ ] Test CI/CD pipeline
  - [ ] Push code to repository
  - [ ] Verify CI pipeline triggers
  - [ ] Verify all jobs pass
  - [ ] Fix any failing jobs

---

## Deliverables Checklist

- [ ] Running development environment (all services start without errors)
- [ ] Database with initial schema (all tables, indexes, triggers created)
- [ ] Backend skeleton with health check endpoint (accessible and returns 200)
- [ ] Frontend skeleton with landing page (displays welcome page)
- [ ] Docker Compose setup working (all containers running)
- [ ] CI/CD pipeline running (builds and tests pass)
- [ ] All test infrastructure in place (tests can be executed)
- [ ] Documentation complete (README, CONTRIBUTING, API docs)

---

## Notes

- **Dependencies**: Some tasks must be completed in order (e.g., Docker Compose before testing full stack)
- **Blockers**: Document any blockers or issues encountered
- **Time Tracking**: Track actual time spent vs estimated time
- **Questions**: Document any unclear requirements or decisions needed

---

## Progress Tracking

**Started**: [Date]
**Completed**: [Date]
**Actual Duration**: [X days]
**Status**: 🔴 Not Started | 🟡 In Progress | 🟢 Completed

---

## Team Assignments (Optional)

| Task Category            | Assigned To | Status |
|--------------------------|-------------|--------|
| Backend - Project Init   |             |        |
| Backend - Database       |             |        |
| Backend - Security       |             |        |
| Backend - Kafka          |             |        |
| Backend - Spring AI      |             |        |
| Backend - Testing        |             |        |
| Frontend - Project Setup |             |        |
| Frontend - Environment   |             |        |
| Frontend - Core Services |             |        |
| Frontend - Routing       |             |        |
| DevOps - Docker          |             |        |
| DevOps - CI/CD           |             |        |
| DevOps - Documentation   |             |        |

---

## Post-Iteration Review

**What went well**:
- [To be filled after iteration]

**What could be improved**:
- [To be filled after iteration]

**Blockers encountered**:
- [To be filled after iteration]

**Lessons learned**:
- [To be filled after iteration]

**Ready for next iteration?**: [ ] Yes [ ] No
- If No, what's missing: _______________
