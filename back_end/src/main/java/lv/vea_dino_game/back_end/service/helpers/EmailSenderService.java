package lv.vea_dino_game.back_end.service.helpers;

import java.util.Properties;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

  private final ResourceLoader resourceLoader;

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

  public void sendEmail(String toUserEmail, String subject, String emailTextMessage) {
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

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("admin@dinogame.com")); // this constructor also checks the string represent the valid email pattern
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toUserEmail));
      message.setSubject(subject);

      Multipart multipart = new MimeMultipart("alternative");
      
      // Text representation
      BodyPart textPart = new MimeBodyPart();
      textPart.setText(emailTextMessage);
      
      // HTML (might wanna add to resourses and parse it from there (TODO LATER []))
      BodyPart htmlPart = new MimeBodyPart();
      String htmlContent = readHtmlFile("ConfirmEmail.html").replace("{%% USERNAME %%}", "solodeni");
      htmlPart.setContent(htmlContent, "text/html");
      
      multipart.addBodyPart(textPart);
      multipart.addBodyPart(htmlPart);

      message.setContent(multipart);

      Transport.send(message);
      System.out.println("Sent message successfully....");
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      System.out.println("UNKNOWN EXCEPTION --> " + e.getMessage());
    }
  }
}
