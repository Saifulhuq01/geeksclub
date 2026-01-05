# GeeksClub REST API Documentation

## Overview

This document describes the RESTful API endpoints for the GeeksClub application. 
All endpoints return JSON responses and use standard HTTP status codes.

**Base URL**: `http://localhost:8080/api`

**API Version**: v1

---

## Table of Contents

1. [Authentication](#authentication)
2. [Users](#users)
3. [Messages](#messages)
4. [Votes](#votes)
5. [Search](#search)
6. [Analytics (Admin)](#analytics-admin)
7. [Common Response Codes](#common-response-codes)
8. [Error Response Format](#error-response-format)

---

## Authentication

### 1. Register User

**Endpoint**: `POST /api/users`

**Description**: Create a new user account.

**Authentication**: Not required

**Request Body**:
```json
{
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "SecureP@ssw0rd123"
}
```

**Success Response** (201 Created):
```json
{
  "id": 1,
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "role": "USER",
  "createdAt": "2025-01-05T10:30:00Z"
}
```

**Error Response** (400 Bad Request):
```json
{
  "timestamp": "2025-01-05T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email already exists"
    },
    {
      "field": "password",
      "message": "Password must be at least 8 characters long"
    }
  ],
  "path": "/api/users"
}
```

**Status Codes**:
- `201`: User created successfully
- `400`: Invalid input or user already exists
- `500`: Internal server error

---

### 2. Login

**Endpoint**: `POST /api/auth/login`

**Description**: Authenticate user and receive JWT token.

**Authentication**: Not required

**Request Body**:
```json
{
  "email": "john.doe@example.com",
  "password": "SecureP@ssw0rd123"
}
```

**Success Response** (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "fullName": "John Doe",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "role": "USER"
  }
}
```

**Error Response** (401 Unauthorized):
```json
{
  "timestamp": "2025-01-05T10:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password",
  "path": "/api/auth/login"
}
```

**Status Codes**:
- `200`: Login successful
- `401`: Invalid credentials
- `400`: Invalid input format
- `500`: Internal server error

---

### 3. Refresh Token

**Endpoint**: `POST /api/auth/refresh`

**Description**: Refresh JWT token before expiration.

**Authentication**: Required (Bearer Token)

**Request Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Success Response** (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "accessTokenExpiresAt": 3600,
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshTokenExpiresAt": 86400
}
```

**Status Codes**:
- `200`: Token refreshed successfully
- `401`: Unauthorized (invalid or expired token)

---

## Users

### 1. Get Current User Profile

**Endpoint**: `GET /api/users/me`

**Description**: Get the authenticated user's profile.

**Authentication**: Required (Bearer Token)

**Success Response** (200 OK):
```json
{
  "id": 1,
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "role": "USER"
}
```

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `404`: User not found

---

### 2. Update User Profile

**Endpoint**: `PUT /api/users/me`

**Description**: Update the authenticated user's profile.

**Authentication**: Required (Bearer Token)

**Request Body**:
```json
{
  "fullName": "John Doe Updated",
  "username": "johndoe_updated"
}
```

**Success Response** (200 OK):
```json
{
  "id": 1,
  "fullName": "John Doe Updated",
  "username": "johndoe_updated",
  "email": "john.doe@example.com",
  "role": "USER"
}
```

**Status Codes**:
- `200`: Profile updated successfully
- `400`: Invalid input or username/email already taken
- `401`: Unauthorized
- `404`: User not found

---

### 3. Get User by Username

**Endpoint**: `GET /api/users/{username}`

**Description**: Get public profile of a user by username.

**Authentication**: Optional

**Success Response** (200 OK):
```json
{
  "id": 1,
  "fullName": "John Doe",
  "username": "johndoe",
  "role": "USER",
  "createdAt": "2025-01-05T10:30:00Z",
  "stats": {
    "messageCount": 25,
    "voteCount": 150
  }
}
```

**Status Codes**:
- `200`: Success
- `404`: User not found

---

## Messages

### 1. Create Message

**Endpoint**: `POST /api/messages`

**Description**: Create a new message (post).

**Authentication**: Required (Bearer Token)

**Request Body**:
```json
{
  "content": "Just learned about Spring AI! It's amazing how easy it is to integrate AI models into Spring Boot applications. #SpringAI #Java"
}
```

**Success Response** (201 Created):
```json
{
  "id": 42,
  "content": "Just learned about Spring AI! It's amazing how easy it is to integrate AI models into Spring Boot applications. #SpringAI #Java",
  "author": {
    "id": 1,
    "fullName": "John Doe",
    "username": "johndoe"
  },
  "status": "PUBLISHED",
  "isSpam": false,
  "votes": {
    "upvoteCount": 0,
    "downvoteCount": 0,
    "score": 0
  },
  "userVote": null,
  "createdAt": "2025-01-05T16:30:00Z",
  "updatedAt": "2025-01-05T16:30:00Z"
}
```

**Error Response** (400 Bad Request):
```json
{
  "timestamp": "2025-01-05T16:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "content",
      "message": "Content must not be empty"
    },
    {
      "field": "content",
      "message": "Content must not exceed 5000 characters"
    }
  ],
  "path": "/api/messages"
}
```

**Status Codes**:
- `201`: Message created successfully
- `400`: Invalid input
- `401`: Unauthorized
- `500`: Internal server error

---

### 2. Get Message Feed

**Endpoint**: `GET /api/messages`

**Description**: Get a paginated list of messages with various sorting options.

**Authentication**: Optional (user vote info included if authenticated)

**Query Parameters**:
- `page` (optional, default: 1): Page number (1-indexed)
- `size` (optional, default: 20, max: 100): Number of messages per page
- `sort` (optional, default: recent): Sorting strategy
  - `recent`: Most recent messages
  - `upvoted`: Most upvoted messages
  - `downvoted`: Most downvoted messages
  - `trending`: Trending based on recent activity

**Example Request**:
```
GET /api/messages?page=0&size=20&sort=upvoted
```

**Success Response** (200 OK):
```json
{
  "content": [
    {
      "id": 42,
      "content": "Just learned about Spring AI! It's amazing how easy it is to integrate AI models into Spring Boot applications. #SpringAI #Java",
      "author": {
        "id": 1,
        "username": "johndoe"
      },
      "status": "PUBLISHED",
      "isSpam": false,
      "votes": {
        "upvoteCount": 145,
        "downvoteCount": 3,
        "score": 142
      },
      "userVote": "UPVOTE",
      "createdAt": "2025-01-05T16:30:00Z"
    },
    {
      "id": 41,
      "content": "Why do programmers prefer dark mode? Because light attracts bugs! 🐛",
      "author": {
        "id": 5,
        "username": "techhumor"
      },
      "status": "PUBLISHED",
      "isSpam": false,
      "votes": {
        "upvoteCount": 89,
        "downvoteCount": 12,
        "score": 77
      },
      "userVote": null,
      "createdAt": "2025-01-05T14:15:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": "upvoted",
    "offset": 0
  },
  "totalPages": 5,
  "totalElements": 95,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "numberOfElements": 20,
  "empty": false
}
```

**Status Codes**:
- `200`: Success
- `400`: Invalid query parameters

---

### 3. Get Message by ID

**Endpoint**: `GET /api/messages/{id}`

**Description**: Get a specific message by its ID.

**Authentication**: Optional (user vote info included if authenticated)

**Success Response** (200 OK):
```json
{
  "id": 42,
  "content": "Just learned about Spring AI! It's amazing how easy it is to integrate AI models into Spring Boot applications. #SpringAI #Java",
  "author": {
    "id": 1,
    "fullName": "John Doe",
    "username": "johndoe"
  },
  "status": "PUBLISHED",
  "isSpam": false,
  "spamConfidence": 0.0234,
  "votes": {
    "upvoteCount": 145,
    "downvoteCount": 3,
    "score": 142
  },
  "userVote": "UPVOTE",
  "createdAt": "2025-01-05T16:30:00Z",
  "updatedAt": "2025-01-05T16:30:00Z"
}
```

**Status Codes**:
- `200`: Success
- `404`: Message not found

---

### 4. Get User's Messages

**Endpoint**: `GET /api/messages?user={username}`

**Description**: Get all messages posted by a specific user.

**Authentication**: Optional

**Query Parameters**:
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20, max: 100): Number of messages per page

**Success Response** (200 OK):
```json
{
  "content": [
    {
      "id": 42,
      "content": "Just learned about Spring AI! It's amazing...",
      "author": {
        "id": 1,
        "fullName": "John Doe",
        "username": "johndoe"
      },
      "status": "PUBLISHED",
      "isSpam": false,
      "votes": {
        "upvoteCount": 145,
        "downvoteCount": 3,
        "score": 142
      },
      "userVote": null,
      "createdAt": "2025-01-05T16:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalPages": 2,
  "totalElements": 25,
  "last": false,
  "first": true
}
```

**Status Codes**:
- `200`: Success
- `404`: User not found

---

### 5. Delete Message

**Endpoint**: `DELETE /api/messages/{id}`

**Description**: Delete a message (only author or admin can delete).

**Authentication**: Required (Bearer Token)

**Success Response** (204 No Content):
```
(empty body)
```

**Status Codes**:
- `204`: Message deleted successfully
- `401`: Unauthorized
- `403`: Forbidden (not the author or admin)
- `404`: Message not found

---

## Votes

### 1. Vote on Message

**Endpoint**: `POST /api/messages/{messageId}/vote`

**Description**: Upvote or downvote a message. If user already voted, update the vote.

**Authentication**: Required (Bearer Token)

**Request Body**:
```json
{
  "voteType": "UP"
}
```

**Vote Types**: `UP` or `DOWN`

**Success Response** (200 OK):
```json
{
  "messageId": 42,
  "voteType": "UP",
  "votes": {
    "upvoteCount": 146,
    "downvoteCount": 3,
    "score": 143
  },
  "votedAt": "2025-01-05T17:00:00Z"
}
```

**Status Codes**:
- `200`: Vote recorded/updated successfully
- `400`: Invalid vote type
- `401`: Unauthorized
- `404`: Message not found
- `409`: Cannot vote on own message

---

### 2. Remove Vote

**Endpoint**: `DELETE /api/messages/{messageId}/vote`

**Description**: Remove user's vote from a message.

**Authentication**: Required (Bearer Token)

**Success Response** (200 OK):
```json
{
  "messageId": 42,
  "votes": {
    "upvoteCount": 145,
    "downvoteCount": 3,
    "score": 142
  },
  "message": "Vote removed successfully"
}
```

**Status Codes**:
- `200`: Vote removed successfully
- `401`: Unauthorized
- `404`: Message or vote not found

---

### 3. Get User's Vote on Message

**Endpoint**: `GET /api/messages/{messageId}/vote`

**Description**: Get the authenticated user's vote on a specific message.

**Authentication**: Required (Bearer Token)

**Success Response** (200 OK):
```json
{
  "messageId": 42,
  "voteType": "UP",
  "votedAt": "2025-01-05T17:00:00Z"
}
```

**No Vote Response** (200 OK):
```json
{
  "messageId": 42,
  "voteType": null,
  "votedAt": null
}
```

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `404`: Message not found

---

## Search

### 1. Search Messages

**Endpoint**: `GET /api/messages/search`

**Description**: Search messages using full-text search.

**Authentication**: Optional

**Query Parameters**:
- `q` (required): Search query string
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20): Number of results per page

**Example Request**:
```
GET /api/messages/search?q=spring+boot+kafka&page=0&size=20
```

**Success Response** (200 OK):
```json
{
  "content": [
    {
      "id": 42,
      "content": "Just learned about Spring AI! It's amazing how easy it is to integrate AI models into Spring Boot applications. #SpringAI #Java",
      "author": {
        "id": 1,
        "fullName": "John Doe",
        "username": "johndoe"
      },
      "status": "PUBLISHED",
      "isSpam": false,
      "votes": {
        "upvoteCount": 145,
        "downvoteCount": 3,
        "score": 142
      },
      "userVote": null,
      "createdAt": "2025-01-05T16:30:00Z",
      "relevanceScore": 0.8765
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalPages": 1,
  "totalElements": 12,
  "query": "spring boot kafka"
}
```

**Status Codes**:
- `200`: Success
- `400`: Missing or invalid search query

---

## Analytics (Admin)

### 1. Get System Overview

**Endpoint**: `GET /api/admin/analytics/overview`

**Description**: Get high-level system metrics.

**Authentication**: Required (Admin role)

**Success Response** (200 OK):
```json
{
  "totalUsers": 1523,
  "activeUsersLast7Days": 342,
  "totalMessages": 8742,
  "messagesLast24Hours": 156,
  "totalVotes": 45623,
  "votesLast24Hours": 892,
  "spamDetected": 234,
  "spamRate": 0.0267,
  "timestamp": "2025-01-05T18:00:00Z"
}
```

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `403`: Forbidden (requires admin role)

---

### 2. Get Daily Statistics

**Endpoint**: `GET /api/admin/analytics/daily`

**Description**: Get daily aggregated statistics.

**Authentication**: Required (Admin role)

**Query Parameters**:
- `days` (optional, default: 30): Number of days to retrieve

**Example Request**:
```
GET /api/admin/analytics/daily?days=7
```

**Success Response** (200 OK):
```json
{
  "statistics": [
    {
      "date": "2025-01-05",
      "messageCount": 156,
      "activeUsers": 89,
      "spamCount": 4,
      "totalVotes": 892
    },
    {
      "date": "2025-01-04",
      "messageCount": 142,
      "activeUsers": 76,
      "spamCount": 3,
      "totalVotes": 823
    }
  ],
  "period": {
    "startDate": "2024-12-29",
    "endDate": "2025-01-05",
    "days": 7
  }
}
```

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `403`: Forbidden (requires admin role)

---

### 3. Get Most Active Users

**Endpoint**: `GET /api/admin/analytics/users/active`

**Description**: Get a list of most active users.

**Authentication**: Required (Admin role)

**Query Parameters**:
- `limit` (optional, default: 20): Number of users to return

**Success Response** (200 OK):
```json
{
  "users": [
    {
      "userId": 1,
      "fullName": "John Doe",
      "username": "johndoe",
      "email": "john.doe@example.com",
      "messageCount": 247,
      "voteCount": 1523,
      "totalActivity": 1770,
      "lastActivityAt": "2025-01-05T17:45:00Z",
      "joinedAt": "2024-11-15T10:30:00Z"
    },
    {
      "userId": 5,
      "fullName": "Tech Humour",
      "username": "techhumor",
      "email": "tech@humor.com",
      "messageCount": 189,
      "voteCount": 892,
      "totalActivity": 1081,
      "lastActivityAt": "2025-01-05T16:20:00Z",
      "joinedAt": "2024-12-01T14:00:00Z"
    }
  ],
  "limit": 20
}
```

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `403`: Forbidden (requires admin role)

---

### 4. Get Trending Messages

**Endpoint**: `GET /api/admin/analytics/messages/trending`

**Description**: Get trending messages based on recent activity.

**Authentication**: Required (Admin role)

**Query Parameters**:
- `limit` (optional, default: 20): Number of messages to return
- `days` (optional, default: 7): Time window for trending calculation

**Success Response** (200 OK):
```json
{
  "messages": [
    {
      "id": 42,
      "content": "Just learned about Spring AI! It's amazing...",
      "author": {
        "id": 1,
        "fullName": "John Doe",
        "username": "johndoe"
      },
      "upvoteCount": 145,
      "downvoteCount": 3,
      "score": 142,
      "recentVotes": 67,
      "createdAt": "2025-01-05T16:30:00Z"
    }
  ],
  "period": {
    "days": 7,
    "startDate": "2024-12-29"
  }
}
```

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `403`: Forbidden (requires admin role)

---

### 5. Get Spam Statistics

**Endpoint**: `GET /api/admin/analytics/spam`

**Description**: Get spam detection statistics.

**Authentication**: Required (Admin role)

**Success Response** (200 OK):
```json
{
  "totalMessages": 8742,
  "spamDetected": 234,
  "spamRate": 0.0267,
  "averageConfidence": 0.8923,
  "byDate": [
    {
      "date": "2025-01-05",
      "totalMessages": 156,
      "spamCount": 4,
      "spamRate": 0.0256
    }
  ],
  "flaggedMessages": [
    {
      "id": 123,
      "content": "Buy cheap products now! Click here...",
      "spamConfidence": 0.9876,
      "status": "FLAGGED",
      "createdAt": "2025-01-05T15:30:00Z"
    }
  ]
}
```

**Status Codes**:
- `200`: Success
- `401`: Unauthorized
- `403`: Forbidden (requires admin role)

---

### 6. Review Flagged Message

**Endpoint**: `PUT /api/admin/messages/{id}/review`

**Description**: Admin review of a flagged message.

**Authentication**: Required (Admin role)

**Request Body**:
```json
{
  "action": "APPROVE",
  "notes": "False positive - legitimate tech announcement"
}
```

**Actions**: `APPROVE`, `REMOVE`

**Success Response** (200 OK):
```json
{
  "id": 123,
  "status": "PUBLISHED",
  "reviewedBy": "admin_user",
  "reviewedAt": "2025-01-05T18:30:00Z",
  "notes": "False positive - legitimate tech announcement"
}
```

**Status Codes**:
- `200`: Review processed successfully
- `400`: Invalid action
- `401`: Unauthorized
- `403`: Forbidden (requires admin role)
- `404`: Message not found

---

## Common Response Codes

| Status Code | Description                                            |
|-------------|--------------------------------------------------------|
| 200         | OK - Request succeeded                                 |
| 201         | Created - Resource created successfully                |
| 204         | No Content - Request succeeded with no response body   |
| 400         | Bad Request - Invalid input or validation error        |
| 401         | Unauthorized - Missing or invalid authentication token |
| 403         | Forbidden - Insufficient permissions                   |
| 404         | Not Found - Resource not found                         |
| 409         | Conflict - Request conflicts with current state        |
| 429         | Too Many Requests - Rate limit exceeded                |
| 500         | Internal Server Error - Server encountered an error    |
| 503         | Service Unavailable - Server temporarily unavailable   |

---

## Error Response Format

All error responses follow this standardized format:

```json
{
  "timestamp": "2025-01-05T18:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed error message",
  "errors": [
    {
      "field": "fieldName",
      "message": "Field-specific error message"
    }
  ],
  "path": "/api/endpoint"
}
```

**Fields**:
- `timestamp`: ISO 8601 timestamp of when the error occurred
- `status`: HTTP status code
- `error`: HTTP status text
- `message`: Human-readable error description
- `errors`: Array of field-level validation errors (optional)
- `path`: API endpoint that generated the error

---

## Rate Limiting

**Rate Limits**:
- **Authenticated users**: 100 requests per minute
- **Unauthenticated users**: 20 requests per minute
- **Admin endpoints**: 200 requests per minute

**Rate Limit Headers**:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1704470400
```

**Rate Limit Exceeded Response** (429):
```json
{
  "timestamp": "2025-01-05T18:30:00Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Please try again later.",
  "retryAfter": 60,
  "path": "/api/messages"
}
```

---

## Authentication Headers

All authenticated endpoints require the JWT token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Pagination

All paginated endpoints return responses in this format:

```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0
  },
  "totalPages": 5,
  "totalElements": 95,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "numberOfElements": 20,
  "empty": false
}
```

---

## CORS Configuration

The API supports CORS for frontend applications:

**Allowed Origins**: Configured via application properties
**Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS
**Allowed Headers**: Authorization, Content-Type
**Max Age**: 3600 seconds

---

## Versioning

API versioning is handled via Request Header `API_VERSION`:
- Current version: `v1` (default)

---

## Health Check

**Endpoint**: `GET /actuator/health`

**Description**: Check API health status.

**Authentication**: Not required

**Success Response** (200 OK):
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "kafka": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

---

## Notes

1. All timestamps are in ISO 8601 format (UTC timezone)
2. All endpoints accept and return `application/json` content type
3. Passwords are never returned in any API response
4. Soft-deleted resources return 404 status
5. Boolean fields use lowercase `true`/`false`
6. Null values are included in responses for optional fields
7. Empty arrays are returned as `[]`, not null
8. Field names use camelCase convention
