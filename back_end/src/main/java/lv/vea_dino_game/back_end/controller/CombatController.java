package lv.vea_dino_game.back_end.controller;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.repo.ICombatRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.ICombatService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/combat")
@RequiredArgsConstructor
public class CombatController {

    private final IPlayerRepo playerRepo;
    private final ICombatService combatService;

    @GetMapping("/find/one/{id}")
    public ResponseEntity<Player> getRandomPlayerThatCanBeAttackedRn(@PathVariable("id") Integer id){
        Optional<Player> currentPlayer = playerRepo.findById(id);
        if (currentPlayer.isPresent()) {
            Optional<Player> result = playerRepo.findRandomOpponentByLevelAndWithoutImmunity(
                    id,
                    LocalDateTime.now(),
                    currentPlayer.get().getLevel()
            );
            if (result.isPresent()) return new ResponseEntity<Player>(result.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/attack/one/{attackerId}/{defenderId}")
    public ResponseEntity<Combat> postAttackSelectedPlayerOnArena(@PathVariable("attackerId") Integer attackerId, @PathVariable("defenderId") Integer defenderId) {
        combatService.attackSelectedPlayerOnArena(attackerId, defenderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
