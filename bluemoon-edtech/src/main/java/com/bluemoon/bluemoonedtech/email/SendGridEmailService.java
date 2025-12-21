package com.bluemoon.bluemoonedtech.email;

import com.sendgrid.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.Mail;
import java.io.IOException;
@Async
@Service
@RequiredArgsConstructor
public class SendGridEmailService implements EmailService {

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    @Override
    public void sendOtp(String toEmail, String otp) {
        System.out.println("3️⃣ Email sending STARTED");

        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        String subject = "Your OTP Code";

        Content content = new Content(
                "text/plain",
                "Your OTP is: " + otp +
                        "\n\nThis OTP is valid for 10 minutes." +
                        "\nIf you didn’t request this, please ignore."
        );

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);
        mail.addPersonalization(new com.sendgrid.helpers.mail.objects.Personalization() {{
            addTo(to);
        }});

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

}
