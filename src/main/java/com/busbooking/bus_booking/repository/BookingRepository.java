package com.busbooking.bus_booking.repository;


import com.busbooking.bus_booking.model.Booking;
import com.busbooking.bus_booking.model.Bus;
import com.busbooking.bus_booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBus(Bus bus);
    List<Booking> findByUser(User user);
    Booking findByBookingId(String bookingId);
}