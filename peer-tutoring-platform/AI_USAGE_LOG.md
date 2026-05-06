# AI Usage Log and Reflection Report

## Assignment: Peer Tutoring Marketplace
## Student: Anwesa Banerjee
## BITS ID: 2023MT93340
## Date: April 29, 2026

---

## AI Tools Used

**Primary Tool**: Claude Code (Anthropic)

---

## Detailed Prompt Log

### 1. Project Architecture Design

**Prompt:**
"I need to build a Peer Tutoring Marketplace for my full-stack assignment. The tech stack is:
- Backend: Java (Spring Boot)
- Frontend: React
- Database: PostgreSQL

Requirements:
- Students can request/offer tutoring sessions
- Ratings after each session
- Admin moderates profiles and payouts
- Three roles: STUDENT, TUTOR, ADMIN

Design the database schema and project structure for me."

**AI Response:**
- Generated 6 entity classes (User, TutorProfile, Session, Booking, Rating, Payout)
- Defined relationships and constraints
- Suggested package structure

**Manual Modifications:**
- Added additional validation annotations
- Added `@PrePersist` for rating validation
- Adjusted field names for consistency

---

### 2. Spring Boot Security Configuration

**Prompt:**
"Create JWT-based authentication for Spring Boot with:
- Three roles: ROLE_STUDENT, ROLE_TUTOR, ROLE_ADMIN
- JWT token generation and validation
- Spring Security configuration with CORS
- Protected endpoints based on roles"

**AI Response:**
- JwtUtil.java for token operations
- JwtAuthenticationFilter.java for request filtering
- UserDetailsImpl.java for user principal
- SecurityConfig.java for security rules

**Manual Modifications:**
- Changed default CORS origins to match frontend
- Adjusted JWT expiration time to 24 hours
- Added role-based endpoint protection

---

### 3. REST API Controllers

**Prompt:**
"Create REST API controllers for:
1. AuthController - login, register, get current user
2. SessionController - CRUD for tutoring sessions
3. BookingController - student booking, tutor approval/rejection
4. TutorController - tutor profiles, ratings
5. AdminController - user management, payouts

Include proper annotations, validation, and security annotations."

**AI Response:**
- Generated all 5 controller classes
- Added @PreAuthorize annotations
- Included proper HTTP status codes
- Added request/response DTOs

**Manual Modifications:**
- Refined endpoint paths for consistency
- Added custom error messages
- Adjusted request mappings

---

### 4. Service Layer Implementation

**Prompt:**
"Create service classes for:
1. SessionService - create, update, delete with overlap detection
2. BookingService - create booking, confirm, reject, complete
3. TutorService - profile management, search
4. RatingService - submit rating, calculate averages
5. AdminService - user moderation, payouts

Include transaction management and business logic."

**AI Response:**
- Generated all service classes
- Added @Transactional annotations
- Included business logic for booking workflow
- Implemented average rating calculation

**Manual Modifications:**
- Added specific error messages
- Enhanced search functionality
- Added booking conflict detection

---

### 5. React Frontend Setup

**Prompt:**
"Create a React frontend with:
1. Context API for authentication state
2. API service with Axios for backend communication
3. Pages: Home, Login, Register, Dashboard, Tutors
4. Role-based dashboard components (Student, Tutor, Admin)
5. CSS styling with responsive design

Include React Router for navigation."

**AI Response:**
- AuthContext.js for state management
- api.js with service methods
- All page components
- Dashboard components for each role
- App.css with responsive design

**Manual Modifications:**
- Adjusted color scheme
- Added form validation UI
- Refined layout spacing

---

## Code Attribution Summary

| Component | AI-Generated Lines | Manual Lines | Total |
|-----------|-------------------|--------------|-------|
| **Backend** | | | |
| Entity Classes | 180 | 25 | 205 |
| Security Classes | 240 | 35 | 275 |
| Controllers | 280 | 45 | 325 |
| Services | 320 | 60 | 380 |
| DTOs | 120 | 15 | 135 |
| Repositories | 80 | 20 | 100 |
| **Frontend** | | | |
| Context/State | 60 | 15 | 75 |
| API Services | 80 | 25 | 105 |
| Page Components | 280 | 80 | 360 |
| Dashboard Components | 220 | 70 | 290 |
| CSS | 280 | 120 | 400 |
| **Configuration** | | | |
| pom.xml | 120 | 20 | 140 |
| application.properties | 25 | 10 | 35 |
| package.json | 40 | 10 | 50 |
| **TOTAL** | **2,325** | **550** | **2,875** |

---

## Reflection Report

### How AI Helped

1. **Rapid Prototyping**: Generated complete Spring Boot boilerplate in minutes instead of hours
2. **Best Practices**: Learned JWT implementation patterns and React Context usage
3. **Code Consistency**: Maintained uniform coding style across files
4. **Error Handling**: Included proper exception handling and validation
5. **API Design**: Structured REST endpoints following conventions

### Challenges Encountered

1. **JWT Configuration**: Initially had issues with JWT token parsing
   - **Resolution**: Manually debugged and fixed token signing key generation

2. **CORS Issues**: Frontend couldn't connect to backend initially
   - **Resolution**: Adjusted CORS configuration in SecurityConfig

3. **React State Management**: Some issues with auth context persistence
   - **Resolution**: Added localStorage integration for token persistence

4. **Database Relations**: Had to adjust cascade types for proper deletion
   - **Resolution**: Modified entity relationships manually

### What Was Learned

1. **Spring Security**: Deep understanding of JWT authentication flow
2. **Transaction Management**: How @Transactional works in Spring
3. **React Hooks**: Better understanding of useEffect and useContext
4. **REST API Design**: Proper resource naming and HTTP method usage
5. **Error Handling**: Global exception handling patterns

### AI Limitations Observed

1. **Context Window**: Sometimes lost track of previous decisions
2. **Edge Cases**: Didn't handle all edge cases (e.g., overlapping bookings)
3. **Testing**: Didn't generate unit tests automatically
4. **Deployment**: No deployment configuration provided

### Manual Work Done

1. **Business Logic**: Refined booking workflow and payment states
2. **Validation**: Added custom validation for session scheduling
3. **UI Refinement**: Adjusted CSS colors, spacing, and responsiveness
4. **Error Messages**: Customized error messages for user clarity
5. **Documentation**: Created comprehensive README and API docs

### Would I Use AI Again?

**Yes**, with the following approach:
- Use AI for boilerplate and initial structure
- Always review and understand generated code
- Test thoroughly after AI generation
- Add manual touches for business-specific logic
- Maintain a log like this for academic honesty

### Comparison: Manual vs AI-Assisted

| Aspect | Manual | AI-Assisted | Winner |
|--------|--------|-------------|--------|
| Development Time | ~40 hours | ~12 hours | AI |
| Code Quality | Good | Good | Tie |
| Understanding | High | Medium | Manual |
| Consistency | Medium | High | AI |
| Debugging Time | Lower | Higher | Manual |
| Learning | High | Medium | Manual |

### Conclusion

AI tools like Claude Code significantly accelerated the development process while maintaining code quality. However, it's essential to:
1. Understand the generated code before using it
2. Test thoroughly
3. Add manual refinements for specific requirements
4. Use it as a learning tool, not a replacement for understanding

---

## Certification

I hereby certify that:
1. This log accurately represents my AI usage
2. I understand all code submitted
3. I have manually tested all functionality
4. I have modified AI-generated code where necessary
5. This reflection report is my own work

**Signature**: Anwesa Banerjee
**Date**: April 29, 2026
