package lv.vea_dino_game.back_end.service.helpers;

import java.util.Properties;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;

import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.model.User;

@Service
public class EmailSenderService {

  private final String FROM_EMAIL = "admin@dinogame.com";

  private final ResourceLoader resourceLoader;

  public EmailSenderService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public String readHtmlFile(String filename) throws IOException {
    Resource resource = resourceLoader.getResource("classpath:static/" + filename);
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }
    }
    return content.toString();
  }
  
  public void sendToAskToConfirmEmail(User user, String confirmationToken) throws Exception {
    String subject = "Confirm your email to complete registration";
    String textContent = "In order to be able to log into your account, please confirm your email by following this link: "
        + "/api/v1/auth/email-confirmation/" + confirmationToken
        + "\n\nIf you never created an account with us, ignore this email.\nWith regards, DinoConflict";

    // {{%%TOKEN%%}} {{%%CURRENT_YEAR%%}}
    String htmlContent;
    try {
      htmlContent = readHtmlFile("ConfirmEmail.html")
                            .replace("{%%USERNAME%%}", user.getUsername())
                            .replace("{{%%TOKEN%%}}", confirmationToken)
                            .replace("{{%%CURRENT_YEAR%%}}", LocalDate.now().getYear() + "");
    } catch (Exception e) {
      System.out.println("ERROR LOG: " + e.getMessage());
      throw new Exception("Unable to parse the email html Temlate");
    }
    sendEmail(user.getEmail(), subject, textContent, htmlContent);
  }

  public void sendEmail(String toUserEmail, String subject, String textContent, String htmlContent) throws Exception {
    Properties properties = new Properties();
    /*
     * At present, we are using the mailtrap.io service for testing purposes. 
     * [ ] TODO: Change to actual SMTP provider for sending mail to real clients + hide sensitive auth data to a separate properties-source
     */

    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io"); // SMTP server address
    properties.put("mail.smtp.port", "587"); // TLS port

    // Authenticate the session
    Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("2e6c57e8ef08f9", "62dc21f9009ec3");
      }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(FROM_EMAIL)); // this constructor also checks the string represent the valid email pattern
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toUserEmail));
    message.setSubject(subject);

    Multipart multipart = new MimeMultipart("alternative");
    
    // Text representation
    BodyPart textPart = new MimeBodyPart();
    textPart.setText(textContent);
    
    BodyPart htmlPart = new MimeBodyPart();
    
    htmlPart.setContent(htmlContent, "text/html");
    
    multipart.addBodyPart(textPart);
    multipart.addBodyPart(htmlPart);

    message.setContent(multipart);

    Transport.send(message);
    System.out.println("Sent message successfully....");
  }
}
