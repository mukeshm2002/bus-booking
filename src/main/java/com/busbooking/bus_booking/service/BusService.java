package com.busbooking.bus_booking.service;

import com.busbooking.bus_booking.exception.BusNotFoundException;
import com.busbooking.bus_booking.exception.SeatNotAvailableException;
import com.busbooking.bus_booking.model.Booking;
import com.busbooking.bus_booking.model.Bus;
import com.busbooking.bus_booking.repository.BookingRepository;
import com.busbooking.bus_booking.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired // இதை மறக்காமல் சேர்த்துள்ளேன்
    private BookingRepository bookingRepository;

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    // Active பஸ்களை மட்டும் எடுக்க (புதிய அம்சம்)
    public List<Bus> getActiveBuses() {
        return busRepository.findByIsActiveTrue();
    }

    public List<Bus> searchByBusNo(String busNo) {
        return busRepository.findByBusNoContainingIgnoreCase(busNo);
    }

    public List<Bus> searchByRoute(String route) {
        return busRepository.findByFromLocationContainingIgnoreCaseOrToLocationContainingIgnoreCase(route, route);
    }

    public List<Bus> getUpcomingBuses() {
        return busRepository.findByDepartureTimeAfterOrderByDepartureTime(LocalDateTime.now());
    }

    public List<Bus> searchBuses(String from, String to, LocalDateTime date) {
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59);
        return busRepository.findByFromLocationAndToLocationAndDepartureTimeBetween(from, to, startOfDay, endOfDay);
    }

    public void deleteBus(int id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException("Bus not found with id: " + id));

        // பஸ்ஸுக்கு புக்கிங் இருந்தால் டெலீட் செய்யக்கூடாது
        List<Booking> bookings = bookingRepository.findByBus(bus);
        if (!bookings.isEmpty()) {
            throw new IllegalStateException("Cannot delete bus with existing bookings. Delete bookings first.");
        }

        busRepository.delete(bus);
    }

    public Bus saveBus(Bus bus) {
        // Validation logic
        if (bus.getBusNo() == null || bus.getBusNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Bus number is required");
        }
        if (bus.getFromLocation() == null || bus.getFromLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("From location is required");
        }
        if (bus.getToLocation() == null || bus.getToLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("To location is required");
        }
        if (bus.getDepartureTime() == null || bus.getArrivalTime() == null) {
            throw new IllegalArgumentException("Departure and Arrival times are required");
        }
        if (bus.getDepartureTime().isAfter(bus.getArrivalTime())) {
            throw new IllegalArgumentException("Departure must be before arrival");
        }
        if (bus.getTotalSeats() <= 0) {
            throw new IllegalArgumentException("Total seats must be positive");
        }
        if (bus.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        return busRepository.save(bus);
    }

    public Bus getBusByBusNo(String busNo) {
        Bus bus = busRepository.findByBusNo(busNo);
        if (bus == null) {
            throw new BusNotFoundException("Bus with number " + busNo + " not found");
        }
        return bus;
    }

    public Bus getBusById(int id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException("Bus with ID " + id + " not found"));
    }

    public void updateAvailableSeats(Bus bus, int seatsBooked) {
        if (bus.getAvailableSeats() < seatsBooked) {
            throw new SeatNotAvailableException("Not enough available seats on bus " + bus.getBusNo());
        }
        bus.setAvailableSeats(bus.getAvailableSeats() - seatsBooked);
        busRepository.save(bus);
    }
}