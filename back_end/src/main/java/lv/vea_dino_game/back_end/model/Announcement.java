package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "announcements")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Announcement {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title cannot be blank/null")
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    private String title;

    @NotBlank(message = "Content cannot be blank/null")
    @Size(min = 4, max = 50, message = "The content must be minimum 4 characters and maximum 50 characters")
    private String content;


    private LocalDateTime date;

    @ManyToOne
    //@JsonBackReference
    @JsonIgnore
    @JoinColumn(name = "clan_id")
    private Clan clan;

    @ManyToOne
    @JsonIgnore
   // @JsonBackReference
    @JoinColumn(name = "player_id")
    private Player author;


    public void setDate(){
        this.date = LocalDateTime.now();
    }

    public Announcement(String title, String content, Clan clan, Player player){
        setTitle(title);
        setContent(content);
        setDate();
        setClan(clan);
        setAuthor(player);
    }



}
