package com.busbooking.bus_booking.controller;

import com.busbooking.bus_booking.model.*;
import com.busbooking.bus_booking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private BusService busService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("search", new Search());
        return "passenger/home";
    }

    @PostMapping("/search")
    public String searchBuses(@ModelAttribute Search search, Model model) {
        LocalDate date = LocalDate.parse(search.getDate());
        List<Bus> buses = busService.searchBuses(search.getFromLocation(), search.getToLocation(), date.atStartOfDay());
        model.addAttribute("buses", buses);
        model.addAttribute("searchDate", search.getDate());
        return "passenger/search-result";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("bookings", bookingService.getBookingsByUser(user));
        return "passenger/profile";
    }

    @GetMapping("/seat-selection/{busNo}")
    public String seatSelection(@PathVariable String busNo, @RequestParam String date, Model model) {
        Bus bus = busService.getBusByBusNo(busNo);
        model.addAttribute("bus", bus);
        model.addAttribute("bookings", bookingService.getBookingsByBus(bus));
        model.addAttribute("date", date);
        return "passenger/seat-selection";
    }

    @PostMapping("/book-seat")
    public String bookSeat(@RequestParam String busNo, @RequestParam String seatNo, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Bus bus = busService.getBusByBusNo(busNo);
        Booking booking = bookingService.createBooking(bus, user, seatNo);
        return "redirect:/passenger/payment?bookingId=" + booking.getBookingId();
    }

    @GetMapping("/payment")
    public String payment(@RequestParam String bookingId, Model model) {
        model.addAttribute("booking", bookingService.getBookingById(bookingId));
        return "passenger/payment";
    }

    public static class Search {
        private String fromLocation, toLocation, date;

        public String getFromLocation() { return fromLocation; }
        public void setFromLocation(String fromLocation) { this.fromLocation = fromLocation; }

        public String getToLocation() { return toLocation; }
        public void setToLocation(String toLocation) { this.toLocation = toLocation; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }
}