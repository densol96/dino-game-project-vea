package lv.vea_dino_game.back_end.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@Table(name = "mail_messages")
@Entity
public class MailMessage {

  @Setter(value = AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull(message = "Mail message must have a from-user")
  @ManyToOne
  @JoinColumn(name = "from_id")
  private User from;

  @NotNull(message = "Mail message must have a to-user")
  @ManyToOne
  @JoinColumn(name = "to_id")
  private User to;

  @NotBlank(message = "Mail message title cannot be blank or empty")
  @Size(min = 2, max=40, message = "Message title should be 2-40 characters long")
  private String title;

  @NotBlank(message = "Mail message cannot be blank/null")
  @Size(min = 5, max = 400, message = "Mail message should be 5-400 characters long")
  private String messageText;

  // from / to but then users can delete those
  @OneToMany(mappedBy = "mail", cascade = CascadeType.ALL)
  List<UserMailMessage> userMailMessages;

  LocalDateTime sentAt = LocalDateTime.now();
}
