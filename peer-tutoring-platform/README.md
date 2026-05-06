# Peer Tutoring Marketplace Platform

A full-stack web application for connecting students with peer tutors. Built with **Spring Boot** (Java) backend and **React** frontend with **PostgreSQL** database.

---

## Features

### User Roles
- **Student**: Browse tutors, book sessions, rate tutors
- **Tutor**: Create sessions, manage bookings, receive ratings
- **Admin**: Moderate profiles, process payouts, view analytics

### Core Functionality
- User Authentication (JWT-based)
- Tutor Profile Management with approval workflow
- Session Scheduling with conflict prevention
- Booking System (Request → Confirm → Complete)
- Rating & Review System
- Admin Dashboard for moderation and payouts

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 3.2, Java 17 |
| Frontend | React 18, React Router 6 |
| Database | PostgreSQL |
| Security | JWT, Spring Security |
| API Docs | OpenAPI/Swagger |

---

## Project Structure

```
peer-tutoring-platform/
├── backend/
│   ├── src/main/java/com/tutoring/
│   │   ├── config/         # Security, CORS config
│   │   ├── controller/     # REST API controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── model/          # JPA Entities
│   │   ├── repository/     # Spring Data JPA repos
│   │   ├── security/       # JWT, UserDetails
│   │   └── service/        # Business logic
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
└── frontend/
    ├── src/
    │   ├── components/     # Reusable components
    │   ├── context/          # AuthContext
    │   ├── pages/            # Page components
    │   ├── services/         # API calls
    │   ├── App.js
    │   └── index.js
    └── package.json
```

---

## Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Maven 3.8+

### Database Setup

```sql
CREATE DATABASE peer_tutoring;
```

Update `backend/src/main/resources/application.properties` with your credentials.

### Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm start
```

Frontend runs on `http://localhost:3000`

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login user |
| GET | `/api/auth/me` | Get current user |

### Sessions
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/sessions` | List all sessions |
| GET | `/api/sessions/available` | Available sessions |
| POST | `/api/sessions` | Create session (Tutor) |
| PUT | `/api/sessions/{id}` | Update session |
| DELETE | `/api/sessions/{id}` | Delete session |

### Bookings
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/student/bookings` | My bookings |
| POST | `/api/student/bookings` | Create booking |
| POST | `/api/tutor/bookings/{id}/confirm` | Confirm booking |
| POST | `/api/tutor/bookings/{id}/reject` | Reject booking |
| POST | `/api/tutor/bookings/{id}/complete` | Mark complete |

### Tutors
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tutors` | List approved tutors |
| GET | `/api/tutors/search` | Search tutors |
| POST | `/api/tutor/profile` | Create profile |
| POST | `/api/student/ratings` | Submit rating |

### Admin
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin/dashboard` | Stats |
| GET | `/api/admin/tutors/pending` | Pending approvals |
| POST | `/api/admin/tutors/{id}/approve` | Approve tutor |
| POST | `/api/admin/payouts` | Create payout |

---

## Database Schema

### Entities
- **User**: id, email, password, firstName, lastName, phoneNumber, role, active
- **TutorProfile**: id, user_id, bio, subject, hourlyRate, status, averageRating
- **Session**: id, tutor_id, title, description, subject, startTime, endTime, price, status
- **Booking**: id, student_id, session_id, amount, status, paymentStatus
- **Rating**: id, booking_id, student_id, tutor_id, rating, review
- **Payout**: id, tutor_id, amount, status, processedBy, processedAt

---

## Screenshots

*Add screenshots here*

---

## Demo Video

[Google Drive Link - Add your video link here]

---

## AI Usage Log

| Task | AI Tool | Prompt Summary | Code Generated |
|------|---------|----------------|----------------|
| Entity Design | Claude | Design database schema for tutoring marketplace | User, Session, Booking, Rating, Payout entities |
| Security Config | Claude | Create JWT authentication | JwtUtil, SecurityConfig, Filters |
| Controllers | Claude | Create REST API endpoints | All controller classes |
| Frontend Setup | Claude | Create React app structure | Components, Pages, Services |
| CSS Styling | Claude | Create responsive CSS | App.css |

---

## Reflection Report

### AI Tools Used
- **Claude Code**: Primary AI assistant for code generation, architecture design, and debugging

### How AI Was Used
1. **Code Generation**: Generated boilerplate for Spring Boot entities, repositories, and service classes
2. **Security Implementation**: JWT authentication and authorization configuration
3. **API Design**: REST endpoint structure and DTOs
4. **Frontend Development**: React components, routing, and API integration
5. **Styling**: Responsive CSS layout

### AI vs Manual Coding
| Component | AI Generated | Modified by Me |
|-----------|-------------|----------------|
| Entity Classes | 90% | 10% (added constraints) |
| Security | 85% | 15% (custom roles) |
| Controllers | 80% | 20% (endpoint paths) |
| Services | 75% | 25% (business logic) |
| React Components | 70% | 30% (UI adjustments) |
| CSS | 60% | 40% (colors, spacing) |

### Benefits
- **Speed**: Rapidly generated boilerplate code
- **Quality**: Consistent code patterns
- **Learning**: Saw best practices for JWT and Spring Security

### Limitations
- **Context Understanding**: Had to refine prompts for specific requirements
- **Edge Cases**: Needed manual handling for overlapping bookings
- **Integration**: Required manual testing and debugging

### Learning Outcomes
- Gained deeper understanding of Spring Security with JWT
- Learned React Context API for state management
- Understood importance of testing AI-generated code

---

## Author

**Student Name**: Anwesa Banerjee

**BITS ID**: 2023MT93340

---

## License

This project is for educational purposes only.
