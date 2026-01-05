# GeeksClub

## 1. Overview

**GeeksClub** is an application that helps techies to share their knowledge and thoughts with the world.

## 2. High-Level Architecture

GeeksClub follows a **modular backend + SPA frontend** architecture.

* **Backend**: Java, Spring Boot, RESTful APIs
* **Frontend**: Angular (Single Page Application)
* **Data Layer**: PostgreSQL
* **Messaging**: Apache Kafka
* **Search**: PostgreSQL Full-Text Search or Elasticsearch
* **AI Integration**: Spring AI for spam detection

## 3. Core Features

### 3.1 User Registration and Authentication

**Description**:
Users can create an account, authenticate, and access protected features of the platform.

**Key Capabilities**:

* User registration with email and password
* Secure password storage (hashing)
* User login and logout
* Role-based access (USER, ADMIN)

**Technical Considerations**:

* Spring Security
* JWT-based authentication
* Validation and error handling

### 3.2 Message Posting

**Description**:
Authenticated users can post messages related to software development, geek jokes, interesting tech announcements, etc.

**Key Capabilities**:

* Create a message with content and metadata
* Automatically publish it, but trigger spam detection in the background
* No attachments (images or videos) for now, simply text content for now

### 3.3 Voting (Upvote / Downvote)

**Description**:
Users can express feedback on messages by upvoting or downvoting them.

**Key Capabilities**:

* One vote per user per message
* Ability to change or remove a vote
* Maintain aggregated vote counts

### 4.4 Message Feeds and Sorting

**Description**:
Users can browse messages using different sorting strategies.

**Sorting Options**:

* Most recent
* Most upvoted
* Most downvoted

**Technical Considerations**:

* Efficient database queries
* Pagination and cursor-based navigation

### 3.5 Message Search

**Description**:
Users can search messages by keywords.

**Implementation Options**:

* PostgreSQL Full-Text Search (initial)
* Elasticsearch (advanced/optional)

**Key Concepts Covered**:

* Search indexing
* Query optimization
* Relevance ranking

### 3.6 Event-Driven Architecture with Kafka

**Description**:
All significant actions in the system publish events to Kafka for decoupled processing and analytics.

**Published Events**:

* `UserRegistered`
* `MessagePosted`
* `MessageUpvoted`
* `MessageDownvoted`

**Use Cases**:

* Analytics
* Activity tracking
* Future integrations

### 3.7 Analytics Dashboard (Admin)

**Description**:
Administrators can view system-wide metrics and user activity.

**Sample Metrics**:

* Total users registered
* Messages posted per day
* Voting activity over time
* Most active users

**Technical Considerations**:

* Event consumption from Kafka
* Read-optimized views
* Time-series aggregation

---

### 3.8 AI-Based Spam Detection

**Description**:
Messages are analyzed for spam or low-quality content using AI models.

**Key Capabilities**:

* Automatic spam classification during message creation
* Flagging or blocking spam messages
* Admin review workflow (optional)

**Technology**:

* Spring AI
* LLM-backed classification models

## 4. Technology Stack

### Backend

* Java (Latest LTS)
* Spring Boot
* Spring WebMVC (REST APIs)
* Spring Security
* Spring Data JPA
* Spring Kafka
* Spring AI

### Frontend

* Angular
* TypeScript
* Tailwind CSS
* PrimeNG (optional)

### Database

* PostgreSQL
* Flyway Database Migrations

### Messaging

* Apache Kafka

### Search

* PostgreSQL Full-Text Search
* Elasticsearch (optional)

### Build & Tooling

* Maven
* Docker & Docker Compose
* Testcontainers (for integration testing)
