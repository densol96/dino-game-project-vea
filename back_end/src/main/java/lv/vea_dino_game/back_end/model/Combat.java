// package lv.vea_dino_game.back_end.model;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.Max;
// import jakarta.validation.constraints.Min;
// import lombok.*;

// import java.time.LocalDateTime;

// @Getter
// @Setter
// @NoArgsConstructor
// @ToString
// @Entity
// @Table(name = "combats")
// public class Combat {

//     @Setter(value = AccessLevel.NONE)
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     public long id;

//     @OneToOne
//     @JoinColumn(name = "id")
//     private CombatResult combatResult;

//     @OneToOne
//     @JoinColumn(name = "id")
//     private CombatHistoryRecap combatHistoryRecap;

//     @ManyToOne
//     @JoinColumn(name = "id")
//     private Player initiator;

//     @ManyToOne
//     @JoinColumn(name = "Id")
//     private Player defender;


//     @Min(value = 1, message = "Minimum player level can not be less than 1")
//     @Max(value = 3, message = "Maximum player level can not be greater than 3")
//     private int level;

//     @Min(value = 10, message = "Turns amount value can not be less than 10")
//     @Max(value = 20, message = "Turns amount value can not greater than 20")
//     private int turnsAmount;

//     private LocalDateTime dateTime;

// }

