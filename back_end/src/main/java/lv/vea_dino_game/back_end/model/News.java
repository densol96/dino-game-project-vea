package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "news")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class News {

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

    public News(String title,String content,LocalDateTime date) {
        setTitle(title);
        setContent(content);
        setDate(date);
    }
}
