package com.busbooking.bus_booking.service;


import com.busbooking.bus_booking.exception.UserNotFoundException;
import com.busbooking.bus_booking.model.User;
import com.busbooking.bus_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String phoneNumber, String email) {
        User user = new User(name, phoneNumber, email);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return user;
    }

    public User getUserByPhone(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UserNotFoundException("User with phone number " + phoneNumber + " not found");
        }
        return user;
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
}