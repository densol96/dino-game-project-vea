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

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 30, message = "Username length must be between 2 and 30 characters")
    @Pattern(regexp = "[A-Z]{1}[a-z]+", message = "First username character must be upper-case and other can be lower-case characters")
    private String username;

    @NotBlank
    // Password will be hashed = pattern constraint enforced on the DTO record
    private String password;

    @NotBlank
    @Size(min = 2, max = 30)
    @Email(message = "Email should be valid")
    private String email;

    private LocalDateTime lastLoggedIn;

    @Enumerated(EnumType.STRING)
    private Role role;

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
