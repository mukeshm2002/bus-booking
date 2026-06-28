package com.busbooking.bus_booking.service;

import com.busbooking.bus_booking.dto.RegisterDTO;
import com.busbooking.bus_booking.model.User;

public interface UserService {
    User registerUser(RegisterDTO registerDTO);
    void registerAdmin(RegisterDTO registerDTO);
    void verifyUserOtp(String email, String otp);
    User getUserByEmail(String email);
}