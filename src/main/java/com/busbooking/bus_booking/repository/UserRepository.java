package com.busbooking.bus_booking.repository;

import com.busbooking.bus_booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Optional பயன்படுத்துவது பாதுகாப்பானது (NullPointerException-ஐத் தவிர்க்கும்)
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    // OTP வெரிஃபிகேஷன் செய்ய
    boolean existsByEmail(String email);
}