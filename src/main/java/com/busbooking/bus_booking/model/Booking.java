package com.busbooking.bus_booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "booking_id", unique = true, nullable = false)
    private String bookingId;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "seat_no", nullable = false)
    private String seatNo;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(nullable = false)
    private String status; // "CONFIRMED", "CANCELLED"

    // Booking.java-வில் சேர்க்க வேண்டியவை:
    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "ticket_pdf_path")
    private String ticketPdfPath; // PDF சேமிக்கப்படும் இடம்

    // Constructors
    public Booking() {}

    public Booking(String bookingId, Bus bus, User user, String seatNo,
                   LocalDateTime bookingTime, String status, double totalPrice) {
        this.bookingId = bookingId;
        this.bus = bus;
        this.user = user;
        this.seatNo = seatNo;
        this.bookingTime = bookingTime;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTicketPdfPath() {
        return ticketPdfPath;
    }

    public void setTicketPdfPath(String ticketPdfPath) {
        this.ticketPdfPath = ticketPdfPath;
    }
}