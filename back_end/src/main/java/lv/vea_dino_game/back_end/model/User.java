package lv.vea_dino_game.back_end.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.Role;

@Data
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Username cannot be blank/null")
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_-]{4,14}", message = "Username must be between 5-15 characters long and can contain letters (A-Z, a-z), digits (0-9), and the special characters _ (underscore) and - (hyphen). The username must start with a letter and cannot contain any other special characters. Example: 'username123', 'user-name', 'user_name'.")
    private String username;

    @NotBlank(message = "Password cannot be blank/null")
    // Password will be hashed = pattern constraint enforced on the DTO record
    private String password;

    @NotBlank(message = "Email cannot be blank/null")
    @Size(max = 30)
    @Email(message = "Email should be valid")
    private String email;

    private LocalDateTime lastLoggedIn;

    private LocalDateTime registrationDate;

    




    @NotNull(message = "Role cannot be null")
    @Enumerated(EnumType.STRING)
    private Role role;


    ////////////////////// Required for game-logic part
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_as_player")
    private Player player;

    public User(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
