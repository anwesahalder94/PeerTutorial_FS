package com.tutoring.config;

import com.tutoring.model.*;
import com.tutoring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TutorProfileRepository tutorProfileRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private PayoutRepository payoutRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // Check if data already exists
        if (userRepository.count() > 0) {
            System.out.println("Database already initialized, skipping...");
            return;
        }

        System.out.println("Initializing test data...");

        // Create Users
        createUsers();

        // Create Tutor Profiles
        createTutorProfiles();

        // Create Sessions
        createSessions();

        // Create Bookings
        createBookings();

        // Create Ratings
        createRatings();

        // Create Payouts
        createPayouts();

        System.out.println("Test data initialization complete!");
    }

    private void createUsers() {
        // Students
        User student1 = new User();
        student1.setFirstName("Alice");
        student1.setLastName("Student");
        student1.setEmail("student1@example.com");
        student1.setPassword(passwordEncoder.encode("Password123!"));
        student1.setPhoneNumber("+1-555-0001");
        student1.setRole(User.Role.STUDENT);
        student1.setActive(true);
        student1.setVerified(true);
        userRepository.save(student1);

        User student2 = new User();
        student2.setFirstName("Charlie");
        student2.setLastName("Learner");
        student2.setEmail("charlie.learner@example.com");
        student2.setPassword(passwordEncoder.encode("Password123!"));
        student2.setPhoneNumber("+1-555-0002");
        student2.setRole(User.Role.STUDENT);
        student2.setActive(true);
        student2.setVerified(true);
        userRepository.save(student2);

        User student3 = new User();
        student3.setFirstName("Emma");
        student3.setLastName("Scholar");
        student3.setEmail("emma.scholar@example.com");
        student3.setPassword(passwordEncoder.encode("Password123!"));
        student3.setPhoneNumber("+1-555-0003");
        student3.setRole(User.Role.STUDENT);
        student3.setActive(true);
        student3.setVerified(false);
        userRepository.save(student3);

        // Tutors
        User tutor1 = new User();
        tutor1.setFirstName("Bob");
        tutor1.setLastName("Tutor");
        tutor1.setEmail("tutor1@example.com");
        tutor1.setPassword(passwordEncoder.encode("Password123!"));
        tutor1.setPhoneNumber("+1-555-1001");
        tutor1.setRole(User.Role.TUTOR);
        tutor1.setActive(true);
        tutor1.setVerified(true);
        userRepository.save(tutor1);

        User tutor2 = new User();
        tutor2.setFirstName("Diana");
        tutor2.setLastName("Educator");
        tutor2.setEmail("diana.tutor@example.com");
        tutor2.setPassword(passwordEncoder.encode("Password123!"));
        tutor2.setPhoneNumber("+1-555-1002");
        tutor2.setRole(User.Role.TUTOR);
        tutor2.setActive(true);
        tutor2.setVerified(true);
        userRepository.save(tutor2);

        User tutor3 = new User();
        tutor3.setFirstName("George");
        tutor3.setLastName("Scientist");
        tutor3.setEmail("george.science@example.com");
        tutor3.setPassword(passwordEncoder.encode("Password123!"));
        tutor3.setPhoneNumber("+1-555-1003");
        tutor3.setRole(User.Role.TUTOR);
        tutor3.setActive(true);
        tutor3.setVerified(true);
        userRepository.save(tutor3);

        User tutor4 = new User();
        tutor4.setFirstName("Hannah");
        tutor4.setLastName("Pending");
        tutor4.setEmail("hannah.pending@example.com");
        tutor4.setPassword(passwordEncoder.encode("Password123!"));
        tutor4.setPhoneNumber("+1-555-1004");
        tutor4.setRole(User.Role.TUTOR);
        tutor4.setActive(true);
        tutor4.setVerified(false);
        userRepository.save(tutor4);

        User tutor5 = new User();
        tutor5.setFirstName("Ian");
        tutor5.setLastName("Rejected");
        tutor5.setEmail("ian.rejected@example.com");
        tutor5.setPassword(passwordEncoder.encode("Password123!"));
        tutor5.setPhoneNumber("+1-555-1005");
        tutor5.setRole(User.Role.TUTOR);
        tutor5.setActive(true);
        tutor5.setVerified(false);
        userRepository.save(tutor5);

        User tutor6 = new User();
        tutor6.setFirstName("Jake");
        tutor6.setLastName("Beginner");
        tutor6.setEmail("jake.beginner@example.com");
        tutor6.setPassword(passwordEncoder.encode("Password123!"));
        tutor6.setPhoneNumber("+1-555-1006");
        tutor6.setRole(User.Role.TUTOR);
        tutor6.setActive(true);
        tutor6.setVerified(true);
        userRepository.save(tutor6);

        // Admins
        User admin1 = new User();
        admin1.setFirstName("Super");
        admin1.setLastName("Admin");
        admin1.setEmail("admin@peertutoring.com");
        admin1.setPassword(passwordEncoder.encode("AdminPass123!"));
        admin1.setPhoneNumber("+1-555-9999");
        admin1.setRole(User.Role.ADMIN);
        admin1.setActive(true);
        admin1.setVerified(true);
        userRepository.save(admin1);

        System.out.println("Created " + userRepository.count() + " users");

        System.out.println("Created " + userRepository.count() + " users");
    }

    private void createTutorProfiles() {
        User tutor1 = userRepository.findByEmail("tutor1@example.com").orElseThrow();
        TutorProfile profile1 = new TutorProfile();
        profile1.setUser(tutor1);
        profile1.setBio("Experienced Java developer with 8 years of teaching experience. I specialize in helping students understand complex programming concepts through practical examples.");
        profile1.setSubject("Computer Science");
        profile1.setSubjects(Arrays.asList("Java", "Python", "Data Structures", "Algorithms"));
        profile1.setHourlyRate(new BigDecimal("500.00"));
        profile1.setYearsOfExperience(8);
        profile1.setAverageRating(4.8);
        profile1.setTotalSessions(45);
        profile1.setStatus(TutorProfile.TutorStatus.APPROVED);
        tutorProfileRepository.save(profile1);

        User tutor2 = userRepository.findByEmail("diana.tutor@example.com").orElseThrow();
        TutorProfile profile2 = new TutorProfile();
        profile2.setUser(tutor2);
        profile2.setBio("Mathematics expert with a passion for making numbers fun and accessible. I teach from basic arithmetic to advanced calculus.");
        profile2.setSubject("Mathematics");
        profile2.setSubjects(Arrays.asList("Algebra", "Calculus", "Geometry", "Statistics"));
        profile2.setHourlyRate(new BigDecimal("400.00"));
        profile2.setYearsOfExperience(5);
        profile2.setAverageRating(4.9);
        profile2.setTotalSessions(32);
        profile2.setStatus(TutorProfile.TutorStatus.APPROVED);
        tutorProfileRepository.save(profile2);

        User tutor3 = userRepository.findByEmail("george.science@example.com").orElseThrow();
        TutorProfile profile3 = new TutorProfile();
        profile3.setUser(tutor3);
        profile3.setBio("Physics and Chemistry tutor with laboratory research experience. I bring real-world applications to theoretical concepts.");
        profile3.setSubject("Science");
        profile3.setSubjects(Arrays.asList("Physics", "Chemistry", "Biology"));
        profile3.setHourlyRate(new BigDecimal("450.00"));
        profile3.setYearsOfExperience(6);
        profile3.setAverageRating(4.6);
        profile3.setTotalSessions(18);
        profile3.setStatus(TutorProfile.TutorStatus.APPROVED);
        tutorProfileRepository.save(profile3);

        User tutor4 = userRepository.findByEmail("hannah.pending@example.com").orElseThrow();
        TutorProfile profile4 = new TutorProfile();
        profile4.setUser(tutor4);
        profile4.setBio("English literature enthusiast and creative writing coach. I help students express themselves clearly and confidently.");
        profile4.setSubject("English");
        profile4.setSubjects(Arrays.asList("English Literature", "Creative Writing", "Grammar"));
        profile4.setHourlyRate(new BigDecimal("350.00"));
        profile4.setYearsOfExperience(3);
        profile4.setAverageRating(0.0);
        profile4.setTotalSessions(0);
        profile4.setStatus(TutorProfile.TutorStatus.PENDING);
        tutorProfileRepository.save(profile4);

        User tutor5 = userRepository.findByEmail("ian.rejected@example.com").orElseThrow();
        TutorProfile profile5 = new TutorProfile();
        profile5.setUser(tutor5);
        profile5.setBio("Former tutor application");
        profile5.setSubject("History");
        profile5.setSubjects(Arrays.asList("World History", "Ancient Civilizations"));
        profile5.setHourlyRate(new BigDecimal("300.00"));
        profile5.setYearsOfExperience(1);
        profile5.setAverageRating(0.0);
        profile5.setTotalSessions(0);
        profile5.setStatus(TutorProfile.TutorStatus.REJECTED);
        tutorProfileRepository.save(profile5);

        User tutor6 = userRepository.findByEmail("jake.beginner@example.com").orElseThrow();
        TutorProfile profile6 = new TutorProfile();
        profile6.setUser(tutor6);
        profile6.setBio("New tutor building experience. I teach basic Chemistry and help students understand fundamental concepts.");
        profile6.setSubject("Chemistry");
        profile6.setSubjects(Arrays.asList("Basic Chemistry", "Organic Chemistry", "Chemical Bonds"));
        profile6.setHourlyRate(new BigDecimal("200.00"));
        profile6.setYearsOfExperience(1);
        profile6.setAverageRating(3.0);
        profile6.setTotalSessions(5);
        profile6.setStatus(TutorProfile.TutorStatus.APPROVED);
        tutorProfileRepository.save(profile6);

        System.out.println("Created " + tutorProfileRepository.count() + " tutor profiles");
    }

    private void createSessions() {
        User tutor1 = userRepository.findByEmail("tutor1@example.com").orElseThrow();
        User tutor2 = userRepository.findByEmail("diana.tutor@example.com").orElseThrow();
        User tutor3 = userRepository.findByEmail("george.science@example.com").orElseThrow();

        // Available sessions
        Session session1 = new Session();
        session1.setTutor(tutor1);
        session1.setTitle("Java Programming Fundamentals");
        session1.setDescription("Learn the basics of Java programming including variables, loops, conditionals, and object-oriented concepts.");
        session1.setSubject("Computer Science");
        session1.setStartTime(LocalDateTime.now().plusDays(5));
        session1.setEndTime(LocalDateTime.now().plusDays(5).plusMinutes(90));
        session1.setPrice(new BigDecimal("500.00"));
        session1.setMaxStudents(1);
        session1.setEnrolledStudents(0);
        session1.setType(Session.SessionType.ONE_ON_ONE);
        session1.setStatus(Session.SessionStatus.AVAILABLE);
        sessionRepository.save(session1);

        Session session2 = new Session();
        session2.setTutor(tutor1);
        session2.setTitle("Advanced Data Structures");
        session2.setDescription("Deep dive into trees, graphs, hash tables, and advanced algorithms.");
        session2.setSubject("Computer Science");
        session2.setStartTime(LocalDateTime.now().plusDays(6));
        session2.setEndTime(LocalDateTime.now().plusDays(6).plusMinutes(90));
        session2.setPrice(new BigDecimal("600.00"));
        session2.setMaxStudents(1);
        session2.setEnrolledStudents(0);
        session2.setType(Session.SessionType.ONE_ON_ONE);
        session2.setStatus(Session.SessionStatus.AVAILABLE);
        sessionRepository.save(session2);

        Session session3 = new Session();
        session3.setTutor(tutor2);
        session3.setTitle("Calculus I - Limits and Derivatives");
        session3.setDescription("Introduction to differential calculus with plenty of practice problems.");
        session3.setSubject("Mathematics");
        session3.setStartTime(LocalDateTime.now().plusDays(5));
        session3.setEndTime(LocalDateTime.now().plusDays(5).plusMinutes(90));
        session3.setPrice(new BigDecimal("400.00"));
        session3.setMaxStudents(1);
        session3.setEnrolledStudents(0);
        session3.setType(Session.SessionType.ONE_ON_ONE);
        session3.setStatus(Session.SessionStatus.AVAILABLE);
        sessionRepository.save(session3);

        Session session4 = new Session();
        session4.setTutor(tutor2);
        session4.setTitle("Group Study: Algebra Review");
        session4.setDescription("Group session covering linear equations, quadratic formulas, and systems of equations.");
        session4.setSubject("Mathematics");
        session4.setStartTime(LocalDateTime.now().plusDays(7));
        session4.setEndTime(LocalDateTime.now().plusDays(7).plusMinutes(90));
        session4.setPrice(new BigDecimal("200.00"));
        session4.setMaxStudents(5);
        session4.setEnrolledStudents(2);
        session4.setType(Session.SessionType.GROUP);
        session4.setStatus(Session.SessionStatus.AVAILABLE);
        sessionRepository.save(session4);

        Session session5 = new Session();
        session5.setTutor(tutor3);
        session5.setTitle("Physics: Mechanics Basics");
        session5.setDescription("Understanding Newton's laws, forces, and motion with interactive examples.");
        session5.setSubject("Science");
        session5.setStartTime(LocalDateTime.now().plusDays(8));
        session5.setEndTime(LocalDateTime.now().plusDays(8).plusMinutes(90));
        session5.setPrice(new BigDecimal("450.00"));
        session5.setMaxStudents(1);
        session5.setEnrolledStudents(0);
        session5.setType(Session.SessionType.ONE_ON_ONE);
        session5.setStatus(Session.SessionStatus.AVAILABLE);
        sessionRepository.save(session5);

        System.out.println("Created " + sessionRepository.count() + " sessions");
    }

    private void createBookings() {
        User student1 = userRepository.findByEmail("student1@example.com").orElseThrow();

        // Get sessions
        List<Session> sessions = sessionRepository.findAll();
        Session session1 = sessions.get(0);
        Session session2 = sessions.get(2);

        // Confirmed booking
        Booking booking1 = new Booking();
        booking1.setStudent(student1);
        booking1.setSession(session1);
        booking1.setAmount(session1.getPrice());
        booking1.setNotes("Looking forward to learning Java!");
        booking1.setStatus(Booking.BookingStatus.CONFIRMED);
        booking1.setPaymentStatus(Booking.PaymentStatus.HELD);
        booking1.setConfirmedAt(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking1);

        // Update session status
        session1.setStatus(Session.SessionStatus.BOOKED);
        session1.setEnrolledStudents(1);
        sessionRepository.save(session1);

        // Completed booking
        Booking booking2 = new Booking();
        booking2.setStudent(student1);
        booking2.setSession(session2);
        booking2.setAmount(session2.getPrice());
        booking2.setNotes("Need help with calculus for my exam.");
        booking2.setStatus(Booking.BookingStatus.COMPLETED);
        booking2.setPaymentStatus(Booking.PaymentStatus.RELEASED_TO_TUTOR);
        booking2.setConfirmedAt(LocalDateTime.now().minusDays(10));
        booking2.setCompletedAt(LocalDateTime.now().minusDays(9));
        bookingRepository.save(booking2);

        System.out.println("Created " + bookingRepository.count() + " bookings");
    }

    private void createRatings() {
        User student1 = userRepository.findByEmail("student1@example.com").orElseThrow();
        User tutor1 = userRepository.findByEmail("tutor1@example.com").orElseThrow();

        List<Booking> bookings = bookingRepository.findAll();
        if (!bookings.isEmpty()) {
            Booking completedBooking = bookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                    .findFirst()
                    .orElse(null);

            if (completedBooking != null) {
                Rating rating = new Rating();
                rating.setBooking(completedBooking);
                rating.setStudent(student1);
                rating.setTutor(tutor1);
                rating.setRating(5);
                rating.setReview("Excellent session! The tutor explained complex concepts very clearly. Highly recommended!");
                rating.setVisible(true);
                ratingRepository.save(rating);

                System.out.println("Created " + ratingRepository.count() + " ratings");
            }
        }
    }

    private void createPayouts() {
        User tutor1 = userRepository.findByEmail("tutor1@example.com").orElseThrow();
        User tutor2 = userRepository.findByEmail("diana.tutor@example.com").orElseThrow();
        User admin = userRepository.findByEmail("admin@peertutoring.com").orElseThrow();

        // Pending payout
        Payout payout1 = new Payout();
        payout1.setTutor(tutor1);
        payout1.setAmount(new BigDecimal("2500.00"));
        payout1.setCurrency("INR");
        payout1.setStatus(Payout.PayoutStatus.PENDING);
        payout1.setPaymentMethod("Bank Transfer");
        payout1.setNotes("Weekly payout for sessions completed");
        payoutRepository.save(payout1);

        // Completed payout
        Payout payout2 = new Payout();
        payout2.setTutor(tutor2);
        payout2.setAmount(new BigDecimal("1800.00"));
        payout2.setCurrency("INR");
        payout2.setStatus(Payout.PayoutStatus.COMPLETED);
        payout2.setPaymentMethod("UPI");
        payout2.setTransactionId("UPI987654321");
        payout2.setNotes("Bi-weekly payout");
        payout2.setProcessedBy(admin);
        payout2.setProcessedAt(LocalDateTime.now().minusDays(5));
        payoutRepository.save(payout2);

        System.out.println("Created " + payoutRepository.count() + " payouts");
    }
}
