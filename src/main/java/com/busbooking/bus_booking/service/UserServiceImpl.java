package com.busbooking.bus_booking.service;

import com.busbooking.bus_booking.dto.RegisterDTO;
import com.busbooking.bus_booking.exception.InvalidInputException;
import com.busbooking.bus_booking.model.User;
import com.busbooking.bus_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @Override
    public User registerUser(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new InvalidInputException("Email already exists!");
        }

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        String otp = String.format("%06d", new Random().nextInt(1000000));

        User user = new User();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setRole("ROLE_USER");
        user.setOtp(otp);
        user.setVerified(false);

        User savedUser = userRepository.save(user);
        emailService.sendOtpEmail(savedUser.getEmail(), otp);
        return savedUser;
    }

    @Override
    public void registerAdmin(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmail())) return;

        User admin = new User();
        admin.setName(registerDTO.getName());
        admin.setEmail(registerDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        admin.setRole("ROLE_ADMIN");
        admin.setVerified(true);
        userRepository.save(admin);
    }

    @Override
    public void verifyUserOtp(String email, String otp) {
        User user = getUserByEmail(email);
        if (user.getOtp() != null && user.getOtp().equals(otp)) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);
        } else {
            throw new InvalidInputException("Invalid OTP.");
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}