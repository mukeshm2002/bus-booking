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

    // PDF ஜெனரேட் செய்யும் சர்வீஸ் (இதற்கென ஒரு தனி கிளாஸ் உருவாக்க வேண்டும்)
    // @Autowired
    // private PdfGeneratorService pdfGeneratorService;

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getBookingsByBus(Bus bus) {
        return bookingRepository.findByBus(bus);
    }

    public Booking getBookingById(String bookingId) {
        return bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with ID " + bookingId + " not found"));
    }

    public Booking createBooking(Bus bus, User user, String seatNo) {
        // Check if seat is already booked
        List<Booking> existingBookings = bookingRepository.findByBus(bus);
        if (existingBookings.stream().anyMatch(b -> b.getSeatNo().equals(seatNo))) {
            throw new SeatNotAvailableException("Seat " + seatNo + " is already booked");
        }

        String bookingId = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // புதிய Booking கன்ஸ்ட்ரக்டருக்கு totalPrice சேர்த்து அப்டேட் செய்துள்ளேன்
        Booking booking = new Booking(bookingId, bus, user, seatNo, LocalDateTime.now(), "CONFIRMED", bus.getPrice());

        // PDF ஜெனரேட் செய்து அந்த பாதையை அப்டேட் செய்யவும்
        // String pdfPath = pdfGeneratorService.generateTicketPdf(booking);
        // booking.setTicketPdfPath(pdfPath);

        busService.updateAvailableSeats(bus, 1);
        return bookingRepository.save(booking);
    }
}