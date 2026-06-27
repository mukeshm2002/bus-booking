package com.busbooking.bus_booking.service;


import com.busbooking.bus_booking.exception.BusNotFoundException;
import com.busbooking.bus_booking.exception.InvalidInputException;
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
    private BookingRepository bookingRepository;


    public List<Bus> getAllBuses() {
        return busRepository.findAll();
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

        // Check if bus has any bookings
        List<Booking> bookings = bookingRepository.findByBus(bus);
        if (!bookings.isEmpty()) {
            throw new IllegalStateException("Cannot delete bus with existing bookings. Delete bookings first.");
        }

        busRepository.delete(bus);
    }

    public Bus saveBus(Bus bus) {
        // Validate required fields
        if (bus.getBusNo() == null || bus.getBusNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Bus number is required");
        }
        if (bus.getFromLocation() == null || bus.getFromLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("From location is required");
        }
        if (bus.getToLocation() == null || bus.getToLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("To location is required");
        }
        if (bus.getDepartureTime() == null) {
            throw new IllegalArgumentException("Departure time is required");
        }
        if (bus.getArrivalTime() == null) {
            throw new IllegalArgumentException("Arrival time is required");
        }
        if (bus.getDepartureTime().isAfter(bus.getArrivalTime())) {
            throw new IllegalArgumentException("Departure must be before arrival");
        }
        if (bus.getTotalSeats() <= 0) {
            throw new IllegalArgumentException("Total seats must be positive");
        }
        if (bus.getAvailableSeats() < 0 || bus.getAvailableSeats() > bus.getTotalSeats()) {
            throw new IllegalArgumentException("Available seats must be between 0 and total seats");
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