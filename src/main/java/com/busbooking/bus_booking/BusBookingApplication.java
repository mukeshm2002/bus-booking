package com.busbooking.bus_booking;

import com.busbooking.bus_booking.dto.RegisterDTO;
import com.busbooking.bus_booking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BusBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusBookingApplication.class, args);
	}

	// அப்ளிகேஷன் ஸ்டார்ட் ஆகும்போது அட்மின் இருக்கிறாரா என்று செக் பண்ணும்
	@Bean
	public CommandLineRunner initAdmin(UserService userService, PasswordEncoder passwordEncoder) {
		return args -> {
			// அட்மின் ஏற்கனவே இல்லை என்றால் மட்டும் உருவாக்கு
			if (userService.getUserByEmail("admin@elitetravel.com") == null) {
				RegisterDTO admin = new RegisterDTO();
				admin.setName("Admin");
				admin.setEmail("admin@elitetravel.com");
				admin.setPassword("admin123"); // இது சர்வீஸ் லேயரில் என்கோட் செய்யப்படும்

				userService.registerAdmin(admin);
				System.out.println("Default Admin account created: admin@elitetravel.com");
			}
		};
	}
}