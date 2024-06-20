package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.MailType;

@Data
@NoArgsConstructor
@Table(name = "user_mail_messages")
@Entity
public class UserMailMessage {
  @Setter(value = AccessLevel.NONE)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @NotNull(message = "user_id cannot be null")
  private User user;

  // 2 or less to one
  @ManyToOne
  @JoinColumn(name = "mail_id")
  @NotNull(message = "mail_id cannot be null")
  private MailMessage mail;

  // from / to
  @NotNull(message = "Type (from/to) cannot be null")
  @Enumerated(EnumType.STRING)
  MailType type;

  Boolean isUnread;
}
