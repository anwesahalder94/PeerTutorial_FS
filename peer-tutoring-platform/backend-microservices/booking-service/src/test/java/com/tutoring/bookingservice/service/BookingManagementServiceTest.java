package com.tutoring.bookingservice.service;

import com.tutoring.bookingservice.client.SessionClient;
import com.tutoring.bookingservice.dto.BookingRequest;
import com.tutoring.bookingservice.model.Booking;
import com.tutoring.bookingservice.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingManagementServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SessionClient sessionClient;

    @InjectMocks
    private BookingManagementService bookingService;

    private Booking booking;
    private BookingRequest bookingRequest;
    private SessionClient.SessionResponse sessionResponse;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setStudentId(1L);
        booking.setSessionId(1L);
        booking.setAmount(BigDecimal.valueOf(50.00));
        booking.setNotes("Please bring calculator");
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);

        bookingRequest = new BookingRequest();
        bookingRequest.setSessionId(1L);
        bookingRequest.setNotes("Please bring calculator");

        sessionResponse = new SessionClient.SessionResponse(
                1L, 2L, "Math Session", "Learn Algebra", "Mathematics",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1),
                BigDecimal.valueOf(50.00), 5, 2, "AVAILABLE",
                false, false
        );
    }

    @Test
    @DisplayName("Should create booking successfully")
    void createBooking_ValidRequest_ReturnsBooking() {
        when(sessionClient.getSessionById(1L)).thenReturn(sessionResponse);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(1L, bookingRequest);

        assertNotNull(result);
        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getSessionId());
        assertEquals(Booking.BookingStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when session not found")
    void createBooking_NonExistingSession_ThrowsException() {
        when(sessionClient.getSessionById(1L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, bookingRequest));
        assertEquals("Session not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when session is full")
    void createBooking_FullSession_ThrowsException() {
        SessionClient.SessionResponse fullSession = new SessionClient.SessionResponse(
                1L, 2L, "Math Session", "Learn Algebra", "Mathematics",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1),
                BigDecimal.valueOf(50.00), 5, 5, "BOOKED",
                true, false
        );
        when(sessionClient.getSessionById(1L)).thenReturn(fullSession);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, bookingRequest));
        assertEquals("Session is already full", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when session is in the past")
    void createBooking_PastSession_ThrowsException() {
        SessionClient.SessionResponse pastSession = new SessionClient.SessionResponse(
                1L, 2L, "Math Session", "Learn Algebra", "Mathematics",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1),
                BigDecimal.valueOf(50.00), 5, 0, "AVAILABLE",
                false, true
        );
        when(sessionClient.getSessionById(1L)).thenReturn(pastSession);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, bookingRequest));
        assertEquals("Cannot book past sessions", exception.getMessage());
    }

    @Test
    @DisplayName("Should get student bookings")
    void getStudentBookings_ReturnsBookings() {
        when(bookingRepository.findByStudentId(1L)).thenReturn(Arrays.asList(booking));

        List<Booking> result = bookingService.getStudentBookings(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get tutor bookings")
    void getTutorBookings_ReturnsBookings() {
        when(sessionClient.getTutorSessions(2L)).thenReturn(Arrays.asList(sessionResponse));
        when(bookingRepository.findBySessionIdIn(Arrays.asList(1L))).thenReturn(Arrays.asList(booking));

        List<Booking> result = bookingService.getTutorBookings(2L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return empty list when tutor has no sessions")
    void getTutorBookings_NoSessions_ReturnsEmptyList() {
        when(sessionClient.getTutorSessions(2L)).thenReturn(Arrays.asList());

        List<Booking> result = bookingService.getTutorBookings(2L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get session bookings")
    void getSessionBookings_ReturnsBookings() {
        when(bookingRepository.findBySessionId(1L)).thenReturn(Arrays.asList(booking));

        List<Booking> result = bookingService.getSessionBookings(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get booking by ID")
    void getBookingById_ExistingBooking_ReturnsBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Should throw exception when booking not found")
    void getBookingById_NonExistingBooking_ThrowsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.getBookingById(1L));
        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should confirm booking successfully")
    void confirmBooking_ValidRequest_ConfirmsBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(sessionClient.getSessionById(1L)).thenReturn(sessionResponse);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.confirmBooking(1L, 2L);

        assertNotNull(result);
        assertEquals(Booking.BookingStatus.CONFIRMED, result.getStatus());
        assertEquals(Booking.PaymentStatus.HELD, result.getPaymentStatus());
        verify(sessionClient).incrementEnrollment(1L);
    }

    @Test
    @DisplayName("Should throw exception when confirming non-pending booking")
    void confirmBooking_NonPendingBooking_ThrowsException() {
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(sessionClient.getSessionById(1L)).thenReturn(sessionResponse);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.confirmBooking(1L, 2L));
        assertEquals("Booking is not in pending status", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when tutor not authorized to confirm")
    void confirmBooking_UnauthorizedTutor_ThrowsException() {
        SessionClient.SessionResponse differentTutorSession = new SessionClient.SessionResponse(
                1L, 3L, "Math Session", "Learn Algebra", "Mathematics",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1),
                BigDecimal.valueOf(50.00), 5, 0, "AVAILABLE",
                false, false
        );
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(sessionClient.getSessionById(1L)).thenReturn(differentTutorSession);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.confirmBooking(1L, 2L));
        assertEquals("Not authorized to confirm this booking", exception.getMessage());
    }

    @Test
    @DisplayName("Should reject booking successfully")
    void rejectBooking_ValidRequest_RejectsBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(sessionClient.getSessionById(1L)).thenReturn(sessionResponse);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.rejectBooking(1L, 2L);

        assertNotNull(result);
        assertEquals(Booking.BookingStatus.CANCELLED, result.getStatus());
        assertEquals(Booking.PaymentStatus.REFUNDED, result.getPaymentStatus());
    }

    @Test
    @DisplayName("Should complete booking successfully")
    void completeBooking_ValidRequest_CompletesBooking() {
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(sessionClient.getSessionById(1L)).thenReturn(sessionResponse);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.completeBooking(1L, 2L);

        assertNotNull(result);
        assertEquals(Booking.BookingStatus.COMPLETED, result.getStatus());
        assertEquals(Booking.PaymentStatus.RELEASED_TO_TUTOR, result.getPaymentStatus());
    }

    @Test
    @DisplayName("Should throw exception when completing non-confirmed booking")
    void completeBooking_NonConfirmedBooking_ThrowsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(sessionClient.getSessionById(1L)).thenReturn(sessionResponse);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.completeBooking(1L, 2L));
        assertEquals("Booking must be confirmed first", exception.getMessage());
    }

    @Test
    @DisplayName("Should cancel booking successfully")
    void cancelBooking_ValidRequest_CancelsBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.cancelBooking(1L, 1L);

        assertNotNull(result);
        assertEquals(Booking.BookingStatus.CANCELLED, result.getStatus());
    }

    @Test
    @DisplayName("Should decrement enrollment when canceling confirmed booking")
    void cancelBooking_ConfirmedBooking_DecrementsEnrollment() {
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        bookingService.cancelBooking(1L, 1L);

        verify(sessionClient).decrementEnrollment(1L);
    }

    @Test
    @DisplayName("Should throw exception when canceling completed booking")
    void cancelBooking_CompletedBooking_ThrowsException() {
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking(1L, 1L));
        assertEquals("Cannot cancel completed booking", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when student not authorized to cancel")
    void cancelBooking_UnauthorizedStudent_ThrowsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking(1L, 999L));
        assertEquals("Not authorized to cancel this booking", exception.getMessage());
    }
}
