package lv.vea_dino_game.back_end.controller;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.enums.DinoType;

import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.ICombatService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/combat")
@RequiredArgsConstructor
public class CombatController {

    private final IPlayerRepo playerRepo;
    private final ICombatService combatService;

    @GetMapping("/find/one/{id}")
    public ResponseEntity<Player> getRandomPlayerThatCanBeAttackedRn(@PathVariable("id") Integer id){
        Optional<Player> currentPlayer = playerRepo.findById(id);
        if (currentPlayer.isPresent()) {
            String oppositeDinoType = (currentPlayer.get().getDinoType() == DinoType.herbivore) ? "carnivore" : "herbivore";

            Optional<Player> result = playerRepo.findRandomOpponentByLevelAndWithoutImmunity(
                    id,
                    LocalDateTime.now(),
                    currentPlayer.get().getLevel(),
                    oppositeDinoType

            );
            if (result.isPresent()) return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/attack/one/{attackerId}/{defenderId}")
    public ResponseEntity<Combat> postAttackSelectedPlayerOnArena(@PathVariable("attackerId") Integer attackerId, @PathVariable("defenderId") Integer defenderId) {
        return new ResponseEntity<Combat>(combatService.attackSelectedPlayerOnArena(attackerId, defenderId), HttpStatus.OK);
    }
}
