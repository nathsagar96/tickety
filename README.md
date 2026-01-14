# Tickety - Event Ticket Platform

Tickety is a modern event ticket platform built with Spring Boot 4 that provides comprehensive ticket management for
event organizers, attendees, and staff. The platform uses Keycloak for authentication and authorization, ensuring secure
access to all features.

## 🚀 Features

### For Organizers

- **Event Management**: Create, update, and delete events with comprehensive details
- **Ticket Types**: Define multiple ticket types with pricing and availability
- **Sales Control**: Set sales windows and manage ticket availability
- **Event Publishing**: Publish events for public viewing when ready

### For Attendees

- **Event Discovery**: Browse and search published events
- **Ticket Purchase**: Secure ticket purchasing with instant confirmation
- **Ticket Management**: View purchased tickets and access QR codes
- **Event Details**: Get comprehensive information about events

### For Staff

- **Ticket Validation**: Validate tickets at event entry points
- **QR Code Scanning**: Scan QR codes for quick ticket verification
- **Validation History**: Track ticket validation status and history

### Platform Features

- **Role-Based Access Control**: Fine-grained permissions using Keycloak roles
- **RESTful API**: Comprehensive API with OpenAPI documentation
- **QR Code Generation**: Automatic QR code generation for tickets
- **Health Monitoring**: Actuator endpoints for system health monitoring
- **Docker Support**: Containerized deployment with Docker Compose

## 📋 Tech Stack

- **Backend**: Spring Boot 4.0
- **Language**: Java 25
- **Database**: PostgreSQL 18
- **Authentication**: Keycloak 26.4
- **API Documentation**: SpringDoc OpenAPI 3.0
- **ORM**: Spring Data JPA with Hibernate
- **QR Codes**: ZXing Library 3.5
- **Build Tool**: Maven
- **Code Quality**: Spotless with Palantir Java Format
- **Object Mapping**: MapStruct 1.6
- **Reduced Boilerplate**: Lombok

## 🔧 Getting Started

### Prerequisites

- Java 25 or higher
- Docker and Docker Compose
- Maven 3.9+
- Git

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/nathsagar96/tickety.git
   cd tickety
   ```

2. **Start the infrastructure**:

   ```bash
   docker compose up -d
   ```

   This will start PostgreSQL and Keycloak containers.

3. **Configure Keycloak**:
   Follow the detailed [Keycloak Setup Guide](KEYCLOAK_SETUP.md) to configure authentication.

4. **Build and run the application**:

   ```bash
   ./mvnw spring-boot:run
   ```

5. **Access the application**:
    - API: `http://localhost:8080`
    - Swagger UI: `http://localhost:8080/swagger-ui.html`
    - Keycloak Admin Console: `http://localhost:9090` (admin/admin)

### Environment Variables

Create a `.env` file or set environment variables:

```bash
# Database configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/postgres
DATABASE_USER=postgres
DATABASE_PASSWORD=SecurePassword123!

# Keycloak configuration
KEYCLOAK_ISSUER_URI=http://localhost:9090/realms/tickety
KEYCLOAK_JWK_SET_URI=http://localhost:9090/realms/tickety/protocol/openid-connect/certs
```

## 🔐 Authentication with Keycloak

Tickety uses Keycloak for authentication and authorization. Refer to the [Keycloak Setup Guide](KEYCLOAK_SETUP.md) for
detailed instructions on:

- Setting up Keycloak realms, clients, and users
- Configuring roles (ORGANIZER, ATTENDEE, STAFF)
- Obtaining access tokens for API requests
- Testing authentication locally

### Getting Access Tokens

After setting up Keycloak, you can obtain an access token:

1. **Using Keycloak Admin Console**:
    - Log in to `http://localhost:9090`
    - Navigate to your realm and client
    - Use the "Get access token" feature

2. **Using cURL**:

   ```bash
   curl -X POST "http://localhost:9090/realms/tickety/protocol/openid-connect/token" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "client_id=tickety-client" \
     -d "username=your-username" \
     -d "password=your-password" \
     -d "grant_type=password"
   ```

3. **Using the token**:
   Include the token in API requests:

   ```bash
   curl -X GET "http://localhost:8080/api/v1/events" \
     -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
   ```

## 📖 API Documentation

The platform provides comprehensive API documentation using SpringDoc OpenAPI:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### API Endpoints

#### Public Endpoints (No Authentication)

- `GET /api/v1/published-events` - Get all published events
- `GET /api/v1/published-events/{eventId}` - Get specific published event

#### Organizer Endpoints (ROLE_ORGANIZER)

- `POST /api/v1/events` - Create new event
- `GET /api/v1/events` - Get my events
- `GET /api/v1/events/{eventId}` - Get event details
- `PUT /api/v1/events/{eventId}` - Update event
- `DELETE /api/v1/events/{eventId}` - Delete event

#### Attendee Endpoints (ROLE_ATTENDEE)

- `POST /api/v1/published-events/{eventId}/ticket-types/{ticketTypeId}/purchase` - Purchase ticket
- `GET /api/v1/tickets` - Get my tickets
- `GET /api/v1/tickets/{ticketId}` - Get ticket details
- `GET /api/v1/tickets/{ticketId}/qr-code` - Get ticket QR code

#### Staff Endpoints (ROLE_STAFF)

- `POST /api/v1/events/{eventId}/validations` - Validate ticket
- `GET /api/v1/events/{eventId}/validations` - Get validation history

## 🎯 Usage Examples

### Create an Event (Organizer)

```bash
curl -X POST "http://localhost:8080/api/v1/events" \
  -H "Authorization: Bearer ORGANIZER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tech Conference 2026",
    "description": "Annual technology conference featuring top speakers and workshops",
    "venue": "Convention Center",
    "startTime": "2026-12-15T09:00:00",
    "endTime": "2026-12-17T18:00:00",
    "salesStart": "2026-01-01T00:00:00",
    "salesEnd": "2026-12-14T23:59:59",
    "status": "PUBLISHED"
  }'
```

### Purchase a Ticket (Attendee)

```bash
curl -X POST "http://localhost:8080/api/v1/published-events/550e8400-e29b-41d4-a716-446655440000/ticket-types/7f3b1e2a-4f5c-6d7e-8f9a-0b1c2d3e4f5a/purchase" \
  -H "Authorization: Bearer ATTENDEE_TOKEN"
```

### Validate a Ticket (Staff)

```bash
curl -X POST "http://localhost:8080/api/v1/events/550e8400-e29b-41d4-a716-446655440000/validations" \
  -H "Authorization: Bearer STAFF_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "ticketId": "7f3b1e2a-4f5c-6d7e-8f9a-0b1c2d3e4f5a",
    "qrCodeValue": "QR123456789",
    "validationMethod": "QR_CODE",
    "notes": "Validated at main entrance"
  }'
```

## 🐳 Docker Deployment

The application is fully containerized:

```bash
# Build Docker image
./mvnw spring-boot:build-image

# Run with Docker Compose
docker compose up -d
```

## 📊 Monitoring

Actuator endpoints are available for monitoring:

- `GET /actuator/health` - Health status
- `GET /actuator/info` - Application info
- `GET /actuator/metrics` - Application metrics

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

1. **Fork the repository** and create your branch from `main`
2. **Follow coding standards**: Use Spotless formatting and Lombok annotations
3. **Write tests**: Ensure all new features have comprehensive test coverage
4. **Update documentation**: Keep README and other docs updated
5. **Submit pull requests** with clear descriptions of changes

### Development Workflow

```bash
# Install dependencies
./mvnw clean install

# Run application in development mode
./mvnw spring-boot:run

# Format code
./mvnw spotless:apply
```

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 📬 Contact

For questions or support, please open an issue on GitHub.
