# Peer Tutoring Marketplace - Architecture Documentation

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   Student    │  │    Tutor     │  │    Admin     │     │
│  │    (UI)      │  │    (UI)      │  │    (UI)      │     │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘     │
└─────────┼────────────────┼────────────────┼───────────────┘
          │                │                │
          └────────────────┴────────────────┘
                           │
                    HTTP/REST API
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                    API Gateway                               │
│              (CORS, Authentication)                          │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                   Application Layer                          │
│                  Spring Boot Backend                         │
│  ┌─────────────┬─────────────┬─────────────┬──────────────┐ │
│  │   Auth      │  Session    │   Booking   │   Admin      │ │
│  │  Service    │  Service    │   Service   │   Service    │ │
│  └──────┬──────┴──────┬──────┴──────┬──────┴──────┬───────┘ │
└─────────┼─────────────┼─────────────┼─────────────┼─────────┘
          │             │             │             │
          └─────────────┴─────────────┴─────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                    Data Layer                                │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Spring Data JPA                         │  │
│  └────────────────────────┬───────────────────────────────┘  │
│                           │                                  │
│  ┌────────────────────────▼───────────────────────────────┐  │
│  │                 PostgreSQL Database                   │  │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐          │  │
│  │  │  User  │ │ Session│ │ Booking│ │ Rating │          │  │
│  │  │  Table │ │  Table │ │  Table │ │  Table │          │  │
│  │  └────────┘ └────────┘ └────────┘ └────────┘          │  │
│  │  ┌────────┐ ┌────────┐                               │  │
│  │  │Tutor   │ │ Payout │                               │  │
│  │  │Profile │ │  Table │                               │  │
│  │  └────────┘ └────────┘                               │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## Component Hierarchy

### Backend Components

```
com.tutoring
├── config
│   └── SecurityConfig           # Security configuration
│
├── controller                   # REST Controllers
│   ├── AuthController          # Authentication endpoints
│   ├── SessionController       # Session management
│   ├── BookingController       # Booking workflow
│   ├── TutorController         # Tutor profiles & ratings
│   └── AdminController         # Admin operations
│
├── service                     # Business Logic Layer
│   ├── SessionService          # Session CRUD + validation
│   ├── BookingService          # Booking workflow
│   ├── TutorService            # Tutor management
│   ├── RatingService           # Rating system
│   └── AdminService            # Admin operations
│
├── repository                  # Data Access Layer
│   ├── UserRepository          # User queries
│   ├── SessionRepository       # Session queries
│   ├── BookingRepository       # Booking queries
│   ├── TutorProfileRepository  # Tutor queries
│   ├── RatingRepository        # Rating queries
│   └── PayoutRepository        # Payout queries
│
├── model                       # Entity Layer
│   ├── User                    # User entity
│   ├── TutorProfile            # Tutor profile
│   ├── Session                 # Tutoring session
│   ├── Booking                 # Booking record
│   ├── Rating                  # Rating record
│   └── Payout                  # Payout record
│
├── security                    # Security Layer
│   ├── JwtUtil                 # JWT utilities
│   ├── JwtAuthenticationFilter # JWT filter
│   ├── UserDetailsImpl         # User principal
│   └── UserDetailsServiceImpl  # User details service
│
└── dto                         # Data Transfer Objects
    ├── LoginRequest            # Login DTO
    ├── SignupRequest           # Registration DTO
    ├── SessionRequest          # Session creation DTO
    ├── BookingRequest          # Booking creation DTO
    ├── RatingRequest           # Rating submission DTO
    ├── TutorProfileRequest     # Profile creation DTO
    ├── PayoutRequest           # Payout creation DTO
    ├── JwtResponse             # Login response DTO
    └── MessageResponse         # Generic message DTO
```

### Frontend Components

```
src/
├── components
│   ├── Navbar.js               # Navigation bar
│   ├── StudentDashboard.js     # Student dashboard
│   ├── TutorDashboard.js       # Tutor dashboard
│   └── AdminDashboard.js       # Admin dashboard
│
├── context
│   └── AuthContext.js          # Authentication context
│
├── pages
│   ├── Home.js                 # Home page
│   ├── Login.js                # Login page
│   ├── Register.js             # Registration page
│   ├── Dashboard.js            # Main dashboard
│   └── Tutors.js               # Tutor listing page
│
├── services
│   └── api.js                  # API service functions
│
├── App.js                      # Main app component
├── App.css                     # Styles
└── index.js                    # Entry point
```

---

## Entity Relationship Diagram (ERD)

```
┌─────────────────┐         ┌──────────────────┐
│      USER       │         │   TUTOR_PROFILE  │
├─────────────────┤         ├──────────────────┤
│ PK id           │◄───────│ PK id            │
│    email        │    1:1  │ FK user_id       │
│    password     │         │    bio           │
│    first_name   │         │    subject       │
│    last_name    │         │    hourly_rate   │
│    phone_number │         │    status        │
│    role         │         │    avg_rating    │
│    active       │         │    total_sessions│
│    verified     │         └──────────────────┘
└────────┬────────┘
         │
         │ 1:N
         │
┌────────┴────────┐
│     SESSION     │
├─────────────────┤
│ PK id           │
│ FK tutor_id     │
│    title        │
│    description  │
│    subject      │
│    start_time   │
│    end_time     │
│    price        │
│    max_students │
│    enrolled     │
│    type         │
│    status       │
└────────┬────────┘
         │
         │ 1:N
         │
┌────────┴────────┐         ┌──────────────────┐
│     BOOKING     │         │      RATING      │
├─────────────────┤         ├──────────────────┤
│ PK id           │◄────────│ PK id            │
│ FK student_id   │    1:1  │ FK booking_id    │
│ FK session_id   │         │ FK student_id    │
│    amount       │         │ FK tutor_id      │
│    notes        │         │    rating        │
│    status       │         │    review        │
│    payment_status│        │    visible       │
│    created_at   │         └──────────────────┘
│    confirmed_at │
│    cancelled_at │
│    completed_at │
└─────────────────┘

┌─────────────────┐
│     PAYOUT      │
├─────────────────┤
│ PK id           │
│ FK tutor_id     │
│ FK processed_by │
│    amount       │
│    currency     │
│    status       │
│    payment_method│
│    transaction_id│
│    notes        │
│    processed_at │
└─────────────────┘
```

---

## API Architecture

### Authentication Flow

```
┌──────────┐                    ┌──────────────┐
│  Client  │─── POST /login ───►│  Auth        │
│          │◄────── JWT ─────────│  Controller  │
└────┬─────┘                    └──────┬───────┘
     │                                   │
     │ JWT Token                         │ validate
     │ (Bearer)                          │ credentials
     │                                   │
     │                    ┌──────────────▼───────┐
     │                    │  UserDetailsService  │
     │                    │  JWT Token Gen         │
     │                    └──────────────────────┘
┌────▼─────┐
│Protected │
│Resource  │◄──── JWT Validation ───┐
│          │                         │
└──────────┘               ┌──────────┴────────┐
                           │  JwtAuthentication │
                           │  Filter            │
                           └───────────────────┘
```

### Request Flow

```
Client Request
     │
     ▼
CORS Filter (SecurityConfig)
     │
     ▼
JwtAuthenticationFilter
     │
     ▼
Spring Security (Authorization)
     │
     ▼
Controller (Request Validation)
     │
     ▼
Service (Business Logic)
     │
     ▼
Repository (Data Access)
     │
     ▼
Database (PostgreSQL)
```

---

## Deployment Architecture

```
┌─────────────────────────────────────────────┐
│              Production Environment          │
│                                              │
│  ┌─────────────────────────────────────┐   │
│  │         Reverse Proxy               │   │
│  │         (Nginx)                   │   │
│  │   Port: 80/443                    │   │
│  └─────────────┬───────────────────────┘   │
│                │                            │
│       ┌────────┴────────┐                  │
│       │                 │                  │
│       ▼                 ▼                  │
│  ┌─────────┐      ┌─────────┐             │
│  │ Frontend│      │ Backend │             │
│  │  React  │      │Spring   │             │
│  │ :3000   │      │Boot:8080│             │
│  └─────────┘      └────┬────┘             │
│                        │                    │
│                        ▼                    │
│                 ┌──────────────┐            │
│                 │ PostgreSQL   │            │
│                 │   :5432      │            │
│                 └──────────────┘            │
│                                              │
└─────────────────────────────────────────────┘
```

---

## Security Architecture

### Authentication
- **JWT Token**: Stateless authentication
- **Expiration**: 24 hours
- **Storage**: LocalStorage (frontend)
- **Transport**: Authorization header (Bearer)

### Authorization
- **Role-based access control**
  - STUDENT: Book sessions, view tutors, submit ratings
  - TUTOR: Create sessions, manage bookings, view earnings
  - ADMIN: Moderate profiles, process payouts, view analytics

### Data Protection
- Password hashing with BCrypt
- CORS configuration for frontend origin
- Input validation with Jakarta Validation
- SQL injection prevention via JPA/Hibernate

---

## Scalability Considerations

### Current Architecture
- Single application instance
- Embedded Tomcat
- Single database

### Future Scalability
- Can be containerized with Docker
- Can use load balancer for multiple instances
- Database can be clustered for read replicas
- Can introduce caching layer (Redis)
- Can add message queue for async processing

---

## Technology Stack Summary

| Layer | Technology | Version |
|-------|------------|---------|
| Frontend | React | 18.2.0 |
| Routing | React Router | 6.20.0 |
| HTTP Client | Axios | 1.6.2 |
| Backend | Spring Boot | 3.2.0 |
| Security | Spring Security | 6.2.0 |
| Data Access | Spring Data JPA | 3.2.0 |
| Database | PostgreSQL | 14+ |
| Build Tool | Maven | 3.8+ |
| Java Version | Java | 17 |

---

## Assumptions

1. **Single Server**: Both frontend and backend run on same server during development
2. **No Payment Gateway**: Payment is simulated with status tracking
3. **No File Upload**: Profile pictures not included in MVP
4. **No Real-time Chat**: Communication outside platform
5. **No Email Service**: Email verification simulated
6. **Single Timezone**: All timestamps in server timezone
