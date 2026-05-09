# Peer Tutoring Platform - Microservices Architecture

This is a microservices-based backend implementation for the Peer Tutoring Platform, built with Spring Boot and Spring Cloud.

## Architecture Overview

```
                    ┌─────────────────┐
                    │   Frontend      │
                    │   (React)       │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  API Gateway    │
                    │   (Port 8080)   │
                    └────────┬────────┘
                             │
         ┌───────────────────┼───────────────────┐
         │                   │                   │
         ▼                   ▼                   ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│  User Service   │ │  Tutor Service  │ │ Session Service│
│   (Port 8081)   │ │   (Port 8082)   │ │  (Port 8083)   │
└─────────────────┘ └─────────────────┘ └─────────────────┘
         │                   │                   │
         │            ┌──────┴───────────────────┘
         │            │
         │            ▼
         │   ┌─────────────────┐
         │   │ Booking Service │
         │   │   (Port 8084)   │
         │   └─────────────────┘
         │
         ▼
┌─────────────────┐
│Service Registry │
│   (Port 8761)   │
│  (Eureka Server)│
└─────────────────┘
```

## Services

### 1. Service Registry (Eureka Server)
- **Port**: 8761
- **Purpose**: Service discovery for all microservices
- **URL**: http://localhost:8761

### 2. API Gateway
- **Port**: 8080
- **Purpose**: Single entry point, JWT authentication, routing
- **Routes**:
  - `/api/auth/**` → User Service
  - `/api/users/**` → User Service
  - `/api/tutors/**` → Tutor Service
  - `/api/sessions/**` → Session Service
  - `/api/bookings/**` → Booking Service

### 3. User Service
- **Port**: 8081
- **Database**: H2 (user_service_db)
- **Domain**: User authentication and management
- **Endpoints**:
  - `POST /api/auth/login` - Login
  - `POST /api/auth/register` - Register
  - `GET /api/users/{id}` - Get user
  - `GET /api/users` - List all users

### 4. Tutor Service
- **Port**: 8082
- **Database**: H2 (tutor_service_db)
- **Domain**: Tutor profiles, ratings, payouts
- **Endpoints**:
  - `POST /api/tutors/profile` - Create profile
  - `GET /api/tutors/profile/{userId}` - Get profile
  - `GET /api/tutors` - List approved tutors
  - `POST /api/tutors/ratings` - Add rating
  - Admin endpoints for approvals and payouts

### 5. Session Service
- **Port**: 8083
- **Database**: H2 (session_service_db)
- **Domain**: Tutoring sessions and availability
- **Endpoints**:
  - `POST /api/sessions` - Create session
  - `GET /api/sessions` - List all sessions
  - `GET /api/sessions/available` - Available sessions
  - `GET /api/sessions/tutor/{tutorId}` - Tutor's sessions
  - Session management (update, delete, cancel)

### 6. Booking Service
- **Port**: 8084
- **Database**: H2 (booking_service_db)
- **Domain**: Bookings and payments
- **Communication**: Feign Client to Session Service
- **Endpoints**:
  - `POST /api/bookings` - Create booking
  - `GET /api/bookings/my-bookings` - Student's bookings
  - `POST /api/bookings/{id}/confirm` - Confirm booking
  - `POST /api/bookings/{id}/cancel` - Cancel booking
  - `POST /api/bookings/{id}/complete` - Complete booking

## Inter-Service Communication

- **Service Discovery**: Eureka Server
- **API Gateway**: Spring Cloud Gateway with JWT validation
- **Inter-service calls**: OpenFeign (Booking Service → Session Service)

## Running the Application

### Prerequisites
- Java 17
- Maven 3.8+

### Running the Application

1. **Start Service Registry** (first):
   ```bash
   cd service-registry
   mvn spring-boot:run
   ```

2. **Start other services** (in any order):
   ```bash
   cd user-service && mvn spring-boot:run &
   cd tutor-service && mvn spring-boot:run &
   cd session-service && mvn spring-boot:run &
   cd booking-service && mvn spring-boot:run &
   ```

3. **Start API Gateway** (last):
   ```bash
   cd api-gateway && mvn spring-boot:run
   ```

### Option 2: Run with Root POM

```bash
cd backend-microservices
mvn clean install
mvn spring-boot:run -pl service-registry
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl tutor-service
mvn spring-boot:run -pl session-service
mvn spring-boot:run -pl booking-service
mvn spring-boot:run -pl api-gateway
```

## API Documentation

Each service has Swagger/OpenAPI documentation:
- User Service: http://localhost:8081/swagger-ui.html
- Tutor Service: http://localhost:8082/swagger-ui.html
- Session Service: http://localhost:8083/swagger-ui.html
- Booking Service: http://localhost:8084/swagger-ui.html

## Key Features

### Microservices Characteristics
1. **Independent Deployment**: Each service can be deployed independently
2. **Database per Service**: Each service has its own database
3. **Loose Coupling**: Services communicate via REST APIs
4. **Service Discovery**: Eureka for service registration and discovery
5. **API Gateway**: Single entry point with cross-cutting concerns (JWT, CORS)
6. **Fault Isolation**: Failure in one service doesn't cascade

### Authentication Flow
1. Client authenticates via `/api/auth/login` (through Gateway)
2. User Service validates credentials and returns JWT
3. Client includes JWT in subsequent requests
4. API Gateway validates JWT and extracts user info
5. Gateway forwards request with `X-User-Id` and `X-User-Role` headers

### Service Communication
- **Synchronous**: REST APIs between services (Feign Client)
- **Load Balancing**: Eureka client-side load balancing

## Ports Summary

| Service | Port | Database |
|---------|------|----------|
| Service Registry | 8761 | N/A |
| API Gateway | 8080 | N/A |
| User Service | 8081 | H2 (user_service_db) |
| Tutor Service | 8082 | H2 (tutor_service_db) |
| Session Service | 8083 | H2 (session_service_db) |
| Booking Service | 8084 | H2 (booking_service_db) |

## Migration from Monolith

The original monolithic application was in `/backend`. Key changes for microservices:

1. **Separated by Domain**: Each microservice owns its domain
2. **Independent Databases**: No shared database tables
3. **Inter-service Communication**: REST APIs instead of in-process calls
4. **Gateway Pattern**: Single entry point instead of direct service access
5. **Service Discovery**: Dynamic service location vs static configuration

## Future Enhancements

1. **Event-Driven Architecture**: Add Kafka/RabbitMQ for async communication
2. **Distributed Tracing**: Add Sleuth and Zipkin
3. **Circuit Breaker**: Add Resilience4j for fault tolerance
4. **Configuration Server**: Spring Cloud Config for externalized config
5. **Separate Databases**: PostgreSQL for each service instead of H2
