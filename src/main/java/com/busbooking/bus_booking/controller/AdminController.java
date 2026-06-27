package com.busbooking.bus_booking.controller;


import com.busbooking.bus_booking.exception.BusNotFoundException;
import com.busbooking.bus_booking.exception.InvalidInputException;
import com.busbooking.bus_booking.model.Booking;
import com.busbooking.bus_booking.model.Bus;
import com.busbooking.bus_booking.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BusService busService;

    @Autowired
    private BusService bookingService;


    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String busNo,
            @RequestParam(required = false) String route,
            Model model) {

        List<Bus> buses;

        if (busNo != null && !busNo.isEmpty()) {
            // Search by bus number
            buses = busService.searchByBusNo(busNo);
        } else if (route != null && !route.isEmpty()) {
            // Search by route (from or to location)
            buses = busService.searchByRoute(route);
        } else {
            // Show all upcoming buses by default
            buses = busService.getUpcomingBuses();
        }

        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("buses", buses);
        model.addAttribute("searchBusNo", busNo);
        model.addAttribute("searchRoute", route);

        return "admin/dashboard";
    }


    @GetMapping("/add-bus")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        return "admin/add-bus";
    }
    @PostMapping("/add-bus")
    public String addBus(@ModelAttribute Bus bus,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {

        // Manual validation
        if (bus.getBusNo() == null || bus.getBusNo().trim().isEmpty()) {
            result.rejectValue("busNo", "error.bus", "Bus number is required");
        }
        if (bus.getDepartureTime() != null && bus.getArrivalTime() != null &&
                bus.getDepartureTime().isAfter(bus.getArrivalTime())) {
            result.rejectValue("departureTime", "error.bus", "Departure must be before arrival");
        }

        if (result.hasErrors()) {
            return "admin/add-bus";
        }

        try {
            busService.saveBus(bus);
            redirectAttributes.addFlashAttribute("successMessage", "Bus added successfully!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error adding bus: " + e.getMessage());
            return "admin/add-bus";
        }
    }

    @GetMapping("/edit-bus/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        try {
            Bus bus = busService.getBusById(id);
            model.addAttribute("bus", bus);
            return "admin/edit-bus";
        } catch (BusNotFoundException e) {
            return "redirect:/admin/dashboard?error=Bus+not+found";
        }
    }

    @PostMapping("/update-bus")
    public String updateBus(@ModelAttribute Bus bus,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (bus.getDepartureTime().isAfter(bus.getArrivalTime())) {
                result.rejectValue("departureTime", "error.bus",
                        "Departure must be before arrival");
                return "admin/edit-bus";
            }

            busService.saveBus(bus);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Bus updated successfully!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error updating bus: " + e.getMessage());
            return "redirect:/admin/edit-bus/" + bus.getId();
        }
    }

    @GetMapping("/delete-bus/{id}")
    public String deleteBus(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            busService.deleteBus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bus deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting bus: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
}