package com.busbooking.bus_booking.repository;


import com.busbooking.bus_booking.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Integer> {
    List<Bus> findByFromLocationAndToLocationAndDepartureTimeBetween(
            String fromLocation, String toLocation, LocalDateTime startDate, LocalDateTime endDate);

    Bus findByBusNo(String busNo);

    @Query("SELECT b FROM Bus b WHERE b.departureTime >= :currentTime")
    List<Bus> findUpcomingBuses(@Param("currentTime") LocalDateTime currentTime);
    List<Bus> findByBusNoContainingIgnoreCase(String busNo);

    List<Bus> findByFromLocationContainingIgnoreCaseOrToLocationContainingIgnoreCase(String fromLocation, String toLocation);

    List<Bus> findByDepartureTimeAfterOrderByDepartureTime(LocalDateTime departureTime);

    // உங்கள் BusRepository-ல் இதையும் சேர்த்துக்கொள்ளுங்கள் (Active buses மட்டும் எடுக்க)
    List<Bus> findByIsActiveTrue();
}