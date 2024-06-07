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
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[A-Z]{1}[a-z]+")
    private String username;

    @NotBlank
    @Size(min = 2, max = 30)
    private String password;

    @NotBlank
    @Size(min = 2, max = 30)
    private String email;

    private LocalDateTime lastLoggedIn;

    @OneToOne
    @JoinColumn(name="user_as_player")
    private Player player;

    public User(String username, String password, String email){
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }
}
