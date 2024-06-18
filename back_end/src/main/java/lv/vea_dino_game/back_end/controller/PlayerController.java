package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.RequestLearnNewPlayerStats;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final IPlayerService playerService;

    @PostMapping("/join/{playerId}/{clanId}")
    public void joinClan(@PathVariable Integer playerId, @PathVariable Integer clanId) {
        playerService.joinClan(playerId, clanId);
    }

    @PostMapping("/enroll/{playerId}")
    public void enrollClan(@PathVariable Integer playerId) {
        playerService.enrollClan(playerId);
    }

    @GetMapping("/sort-level-desc")
    public ResponseEntity<List<Player>> getAllPlayersSortByLevelDesc() {
        return new ResponseEntity<List<Player>>(playerService.getAllPlayersSortByLevelDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-level-asc")
    public ResponseEntity<List<Player>> getAllPlayersSortByLevelAsc() {
        return new ResponseEntity<List<Player>>(playerService.getAllPlayersSortByLevelAsc(), HttpStatus.OK);
    }

    @GetMapping("/find-by-level/{level}")
    public ResponseEntity<List<Player>> getAllPlayersByLevel(@PathVariable Integer level) {
        return new ResponseEntity<List<Player>>(playerService.getAllPlayersByLevel(level), HttpStatus.OK);
    }

//    @GetMapping("/ingame-stats/{playerId}")
//    public ResponseEntity<PlayerStats> getPlayerStats(@PathVariable Integer playerId) {
//        return new ResponseEntity<PlayerStats>(playerService.getPlayerStatsByPlayerId(playerId), HttpStatus.OK);
//    }

    @PostMapping("/ingame-stats")
    public ResponseEntity<BasicMessageResponse> updateNewPlayerStats(@Valid @RequestBody RequestLearnNewPlayerStats newPlayerStats) {
        return new ResponseEntity<BasicMessageResponse>(playerService.updateSkillPoints(newPlayerStats), HttpStatus.OK);
    }
}
