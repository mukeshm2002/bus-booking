package com.busbooking.bus_booking.service;


import com.busbooking.bus_booking.exception.BookingNotFoundException;
import com.busbooking.bus_booking.exception.SeatNotAvailableException;
import com.busbooking.bus_booking.model.Booking;
import com.busbooking.bus_booking.model.Bus;
import com.busbooking.bus_booking.model.User;
import com.busbooking.bus_booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusService busService;

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getBookingsByBus(Bus bus) {
        return bookingRepository.findByBus(bus);
    }

    public Booking getBookingById(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found");
        }
        return booking;
    }

    public Booking createBooking(Bus bus, User user, String seatNo) {
        // Check if seat is already booked
        List<Booking> existingBookings = bookingRepository.findByBus(bus);
        if (existingBookings.stream().anyMatch(b -> b.getSeatNo().equals(seatNo))) {
            throw new SeatNotAvailableException("Seat " + seatNo + " is already booked");
        }

        String bookingId = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Booking booking = new Booking(bookingId, bus, user, seatNo, LocalDateTime.now(), "CONFIRMED");
        busService.updateAvailableSeats(bus, 1);
        return bookingRepository.save(booking);
    }
}