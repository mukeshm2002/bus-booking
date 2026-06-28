package com.busbooking.bus_booking.controller;

import com.busbooking.bus_booking.dto.RegisterDTO;
import com.busbooking.bus_booking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/passenger/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new RegisterDTO());
        return "register";
    }

    @PostMapping("/passenger/register")
    public String registerUser(@ModelAttribute RegisterDTO registerDTO, HttpSession session) {
        // இங்கு நீங்கள் UserService மூலம் OTP ஜெனரேட் செய்து அனுப்ப வேண்டும்
        // உதாரணத்திற்கு செஷனில் OTP-ஐ சேமிக்கிறோம் (பிற்காலத்தில் SMS API பயன்படுத்தவும்)
        String generatedOtp = "123456"; // டெஸ்டிங்கிற்காக
        session.setAttribute("otp", generatedOtp);
        session.setAttribute("tempUser", registerDTO);

        System.out.println("Generated OTP: " + generatedOtp); // கன்சோலில் பார்க்க

        return "redirect:/passenger/verify-otp?email=" + registerDTO.getEmail();
    }

    @GetMapping("/passenger/verify-otp")
    public String showOtpForm(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "verify-otp"; // verify-otp.html பக்கம்
    }

    @PostMapping("/passenger/verify-otp")
    public String verifyOtp(@RequestParam String otp, HttpSession session) {
        String savedOtp = (String) session.getAttribute("otp");
        RegisterDTO tempUser = (RegisterDTO) session.getAttribute("tempUser");

        if (savedOtp != null && savedOtp.equals(otp)) {
            userService.registerUser(tempUser); // OTP சரியாக இருந்தால் மட்டும் யூசரை சேமிக்கும்
            session.removeAttribute("otp");
            session.removeAttribute("tempUser");
            return "redirect:/login?verified";
        }
        return "redirect:/passenger/verify-otp?error=true";
    }
}