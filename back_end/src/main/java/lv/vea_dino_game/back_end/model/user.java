package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "UserTable")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class User {

    @Setter(value = AccessLevel.NONE)
    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[A-Z]{1}[a-z]+")
    @Column(name = "Username")
    private String username;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "Password")
    private String password;


    public User(String username, String password){
        setUsername(username);
        setPassword(password);
    }
}
