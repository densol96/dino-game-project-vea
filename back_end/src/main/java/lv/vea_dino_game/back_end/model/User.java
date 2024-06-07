package lv.vea_dino_game.back_end.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "users")
@Entity
public class User {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 30, message = "Username length must be between 2 and 30 characters")
    @Pattern(regexp = "[A-Z]{1}[a-z]+", message = "First username character must be upper-case and other can be lower-case characters")
    private String username;

    @NotBlank
    @Size(min = 2, max = 30, message = "Password length must be between 2 and 30 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password requires at least 8 characters, with at least one uppercase letter and one number")
    private String password;

    @NotBlank
    @Size(min = 2, max = 30)
    @Email(message = "Email should be valid")
    private String email;

    private LocalDateTime lastLoggedIn;

    @OneToOne
    @JoinColumn(name = "user_as_player")
    private Player player;

    public User(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }
}
