package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CombatHistoryRecap")
public class CombatHistoryRecap {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(mappedBy = "id")
    @ToString.Exclude
    public Combat combat;
}
