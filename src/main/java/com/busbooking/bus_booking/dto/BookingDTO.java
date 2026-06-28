package com.busbooking.bus_booking.dto;

import java.time.LocalDateTime;

public class BookingDTO {
    private String bookingId;
    private String busNo;
    private String seatNo;
    private String passengerName;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String contactNo;

    // புதிய ஃபீல்டுகள் (PDF மற்றும் கட்டணத்திற்கு)
    private double totalPrice;
    private String ticketPdfPath;

    // Constructors
    public BookingDTO() {}

    public BookingDTO(String bookingId, String busNo, String seatNo, String passengerName,
                      LocalDateTime departureTime, LocalDateTime arrivalTime, String contactNo,
                      double totalPrice, String ticketPdfPath) {
        this.bookingId = bookingId;
        this.busNo = busNo;
        this.seatNo = seatNo;
        this.passengerName = passengerName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.contactNo = contactNo;
        this.totalPrice = totalPrice;
        this.ticketPdfPath = ticketPdfPath;
    }

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }

    public String getSeatNo() { return seatNo; }
    public void setSeatNo(String seatNo) { this.seatNo = seatNo; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    // புதிய Getters & Setters
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getTicketPdfPath() { return ticketPdfPath; }
    public void setTicketPdfPath(String ticketPdfPath) { this.ticketPdfPath = ticketPdfPath; }
}