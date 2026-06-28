package com.busbooking.bus_booking.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendOtpEmail(String toEmail, String otp) {
        Email from = new Email("your-verified-sender@yourdomain.com"); // SendGrid-ல் வெரிஃபை செய்த ஈமெயில்
        String subject = "Bus Booking OTP Verification";
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "உங்கள் OTP எண்: " + otp + ". இந்த எண்ணை யாருக்கும் பகிர வேண்டாம்.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}