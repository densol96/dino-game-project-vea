package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "currentJob")
    @JsonIgnore
    public Player player;

    @Min(value = 0, message = "hoursDuration value can not be less than 0")
    private Integer rewardCurrency = 1;

    @Min(value = 0, message = "hoursDuration value can not be less than 0")
    private Integer hoursDuration = 1;

    @NotNull(message = "startDateTime cannot be null")
    private LocalDateTime startDateTime = LocalDateTime.now();

}
