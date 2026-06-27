package com.busbooking.bus_booking.controller;


import com.busbooking.bus_booking.exception.InvalidInputException;
import com.busbooking.bus_booking.exception.UserNotFoundException;
import com.busbooking.bus_booking.model.Booking;
import com.busbooking.bus_booking.model.Bus;
import com.busbooking.bus_booking.model.User;
import com.busbooking.bus_booking.service.BookingService;
import com.busbooking.bus_booking.service.BusService;
import com.busbooking.bus_booking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
        LocalDateTime dateTime = date.atStartOfDay();
        List<Bus> buses = busService.searchBuses(search.getFromLocation(), search.getToLocation(), dateTime);
        model.addAttribute("buses", buses);
        model.addAttribute("searchDate", search.getDate());
        return "passenger/search-result";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "passenger/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, HttpSession session) {
        // Validate input
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new InvalidInputException("Phone number cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }

        // Check if user already exists
        try {
            userService.getUserByEmail(user.getEmail());
            throw new InvalidInputException("Email already registered");
        } catch (UserNotFoundException e) {
            // Expected - email not found, can proceed
        }

        try {
            userService.getUserByPhone(user.getPhoneNumber());
            throw new InvalidInputException("Phone number already registered");
        } catch (UserNotFoundException e) {
            // Expected - phone not found, can proceed
        }

        User registeredUser = userService.registerUser(user.getName(), user.getPhoneNumber(), user.getEmail());
        session.setAttribute("user", registeredUser);
        return "redirect:/passenger/profile";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new UserNotFoundException("Please register or login to view your profile");
        }

        List<Booking> bookings = bookingService.getBookingsByUser(user);
        model.addAttribute("bookings", bookings);
        return "passenger/profile";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // This clears the session
        return "redirect:/passenger/home";
    }

    @GetMapping("/seat-selection/{busNo}")
    public String seatSelection(@PathVariable String busNo, @RequestParam String date, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/passenger/register";
        }

        Bus bus = busService.getBusByBusNo(busNo);
        List<Booking> bookings = bookingService.getBookingsByBus(bus);

        model.addAttribute("bus", bus);
        model.addAttribute("bookings", bookings);
        model.addAttribute("date", date);
        return "passenger/seat-selection";
    }

    @PostMapping("/book-seat")
    public String bookSeat(@RequestParam String busNo, @RequestParam String seatNo,
                           @RequestParam String date, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Bus bus = busService.getBusByBusNo(busNo);

        Booking booking = bookingService.createBooking(bus, user, seatNo);
        return "redirect:/passenger/payment?bookingId=" + booking.getBookingId();
    }

    @GetMapping("/payment")
    public String payment(@RequestParam String bookingId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Booking booking = bookingService.getBookingById(bookingId);

        model.addAttribute("booking", booking);
        model.addAttribute("user", user);
        return "passenger/payment";
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(@RequestParam String bookingId, Model model) {
        Booking booking = bookingService.getBookingById(bookingId);
        model.addAttribute("booking", booking);
        return "passenger/payment-success";
    }

    // Helper class for search form
    public static class Search {
        private String fromLocation;
        private String toLocation;
        private String date;

        // Getters and Setters
        public String getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(String fromLocation) {
            this.fromLocation = fromLocation;
        }

        public String getToLocation() {
            return toLocation;
        }

        public void setToLocation(String toLocation) {
            this.toLocation = toLocation;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}