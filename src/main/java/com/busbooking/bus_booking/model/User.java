package com.busbooking.bus_booking.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    // User.java-வில் சேர்க்க வேண்டியவை:
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // "ROLE_USER" or "ROLE_ADMIN"

    @Column(name = "otp")
    private String otp;

    @Column(name = "is_verified")
    private boolean isVerified = false;

// இதற்கான Getters மற்றும் Setters-ஐச் சேர்த்துக் கொள்ளவும்.

    // Constructors
    public User() {}

    public User(String name, String phoneNumber, String email, String password, String role) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isVerified = false; // ஆரம்பத்தில் வெரிஃபிகேஷன் இருக்காது
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // Password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // OTP
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    // IsVerified
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean isVerified) { this.isVerified = isVerified; }
}