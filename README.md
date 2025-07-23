# Getting Started

### Setup and run instructions

The project is built using Gradle. In order to run the project, you need to have Gradle installed on your machine.
You can download it from the [Gradle website](https://gradle.org/install/).
To build the project and execute tests, execute the following command in the root directory of the project:

```gradle clean build```

Import project into your IDE (e.g., IntelliJ IDEA) and run the main class `TicketingServiceApplication`.

You can also run the project using docker compose by executing the following command in the root directory of the project:

```bash
docker compose up
```

### How to test

Once you have the project running (either using docker compose or by running TicketingServiceApplication file),
you can test the endpoints using tools like Postman or curl.

In addition to the CURL requests provided below, you can also use the postman collection available in the project root directory
TicketingService.postman_collection.json.

Following endpoints are available:

* Create ticket: `POST /api/v1/tickets` : Here is the curl command to create a ticket:

```bash
curl --location 'http://localhost:8080/api/v1/tickets' \
--header 'Content-Type: application/json' \
--data '{
    "userId": "Marko Z",
    "subject": "Payment Issue",
    "description": "I was charged twice for the same order"
}'
```

* Get all tickets: `GET /api/v1/tickets` with optional query parameters `status`, `userId` and `assigneeId` to filter tickets by status or
  user ID or assigneeId. Here is the curl command to get all tickets:

```bash
curl --location 'http://localhost:8080/api/v1/tickets?status=OPEN&userId=Marko%20Z&assigneeId=someAssigneeId'
```

* Update ticket status: `PATCH /api/v1/tickets/{ticketId}/status` where `{ticketId}` is the ID of the ticket you want to update. Here is the
  curl command to update a ticket status:

```bash
curl --location --request PATCH 'http://localhost:8080/api/v1/tickets/{ticketId}/status' \
--header 'Content-Type: application/json' \
--data '{
    "status": "IN_PROGRESS"
}'
```

* Add comment to the ticket: `POST /api/v1/tickets/{ticketId}/comments` where `{ticketId}` is the ID of the ticket you want to add a comment
  to. Here is the curl command to add a comment:

```bash
curl --location 'http://localhost:8080/api/v1/tickets/{ticketId}/comments' \
--header 'Content-Type: application/json' \
--data '{
    "authorId" : "Author",
    "content": "Checked again and same issue",
    "visibility" : "PUBLIC"
}'
```

### Design and Architecture

The project is designed using a microservices architecture and follows the principles of hexagonal architecture. The main components of the
project are:

* **Domain Layer**: Contains the core business logic and domain entities.
* **Application Layer**: Contains the rest controllers and application services that orchestrate the domain logic and handle the use cases.
* **Infrastructure Layer**: Contains the implementation of the repositories, external services, and other infrastructure concerns.
* **API Layer**: Contains the REST models

### AI Tools

For development of this project, GitHub Copilot was used to assist in writing code and generating documentation.


