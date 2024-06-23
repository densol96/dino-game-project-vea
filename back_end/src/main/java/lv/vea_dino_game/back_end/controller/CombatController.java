package lv.vea_dino_game.back_end.controller;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.ArenaSearchPlayerDto;
import lv.vea_dino_game.back_end.model.dto.CombatResultDto;
import lv.vea_dino_game.back_end.model.enums.DinoType;

import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
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
    private final IAuthService authService;

    @GetMapping("/find/one")
    public ResponseEntity<ArenaSearchPlayerDto> getRandomPlayerThatCanBeAttackedRn() {
      return new ResponseEntity<>(combatService.getRandomPlayerThatCanBeAttackedRn(), HttpStatus.OK);
    }

    @GetMapping("/attack/one/{defenderId}")
    public ResponseEntity<CombatResultDto> postAttackSelectedPlayerOnArena(@PathVariable("defenderId") Integer defenderId) {
      return new ResponseEntity<>(combatService.attackSelectedPlayerOnArena(defenderId), HttpStatus.OK);
    }
}
