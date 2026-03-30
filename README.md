# Election App API

A Spring Boot REST API for managing elections, candidates, and voting.

## Features

- Create and manage elections
- Add candidates to elections
- Cast votes with duplicate prevention
- View election results ranked by votes
- Update election status (UPCOMING → ONGOING → ENDED)

## Tech Stack

- Java 25
- Spring Boot 4.1.0
- MongoDB
- Maven
- Lombok

## Prerequisites

- Java 25 or higher
- Maven
- MongoDB running on `localhost:27017`

## Getting Started

1. **Start MongoDB**
   ```bash
   mongod
   ```

2. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The API will start on `http://localhost:8080`

## API Endpoints

### Elections

**Create Election**
```http
POST /elections
Content-Type: application/json

{
  "name": "Presidential Election 2026",
  "startDate": "2026-06-01",
  "endDate": "2026-06-30"
}
```

**Get Election**
```http
GET /elections/{id}
```

**Update Election Status**
```http
PATCH /elections/{id}/status
Content-Type: application/json

{
  "status": "ONGOING"
}
```
Status options: `UPCOMING`, `ONGOING`, `ENDED`

**Get Election Results**
```http
GET /elections/{id}/results
```
Returns candidates ranked by vote count (descending)

### Candidates

**Add Candidate**
```http
POST /candidates
Content-Type: application/json

{
  "name": "John Doe",
  "electionId": "election123"
}
```

### Voting

**Cast Vote**
```http
POST /votes
Content-Type: application/json

{
  "electionId": "election123",
  "candidateId": "candidate456",
  "voterId": "voter789"
}
```

## Business Rules

- Elections start with status `UPCOMING`
- Candidates can only be added to non-ENDED elections
- Votes can only be cast in `ONGOING` elections
- Each voter can vote only once per election
- Candidates must belong to the election being voted on

## Database

MongoDB collections:
- `election` - Stores election data
- `candidates` - Stores candidate information
- `votes` - Records all votes cast

## Testing

Run tests:
```bash
mvn test
```

Tests use `@SpringBootTest` for integration testing with embedded MongoDB.

## Project Structure

```
src/main/java/dreamdev/moniepoint/
├── controllers/       # REST endpoints
├── services/          # Business logic
├── data/
│   ├── models/        # MongoDB entities
│   └── repositories/  # Data access layer
├── dtos/
│   ├── requests/      # API request objects
│   └── responses/     # API response objects
├── exceptions/        # Global exception handling
└── utils/enums/       # Status enums
```

## Error Handling

The API returns structured error responses:

```json
{
  "timestamp": "2026-03-30T10:30:45",
  "status": 400,
  "error": "Bad Request",
  "message": "Election with id 'xyz' does not exist",
  "path": "/elections/xyz"
}
```

Common HTTP status codes:
- `200 OK` - Successful GET/PATCH
- `201 Created` - Successful POST
- `400 Bad Request` - Validation errors
- `500 Internal Server Error` - Unexpected errors
