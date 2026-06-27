package com.busbooking.bus_booking.repository;


import com.busbooking.bus_booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
}