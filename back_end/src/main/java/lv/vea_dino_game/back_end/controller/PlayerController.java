package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.RequestLearnNewPlayerStats;

import lv.vea_dino_game.back_end.model.dto.AllPlayerInfoDto;

import lv.vea_dino_game.back_end.model.dto.PlayerInfoDto;



import lv.vea_dino_game.back_end.service.IPlayerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final IPlayerService playerService;

    @PostMapping("/join_clan/{clanId}")
    public ResponseEntity<BasicMessageResponse> joinClan(@PathVariable Integer clanId) {
        return new ResponseEntity<>(playerService.joinClan(clanId), HttpStatus.CREATED);
    }

    @PostMapping("/exit_clan")
    public ResponseEntity<BasicMessageResponse> exitClan() {
        return new ResponseEntity<>(playerService.exitClan(), HttpStatus.CREATED);

    }

    @GetMapping("/sort-level-desc")
    public ResponseEntity<List<AllPlayerInfoDto>> getAllPlayersSortByLevelDesc() {
        return new ResponseEntity<>(playerService.getAllPlayersSortByLevelDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-level-asc")
    public ResponseEntity<List<AllPlayerInfoDto>> getAllPlayersSortByLevelAsc() {
        return new ResponseEntity<>(playerService.getAllPlayersSortByLevelAsc(), HttpStatus.OK);
    }

    @GetMapping("/find-by-level/{level}")
    public ResponseEntity<List<AllPlayerInfoDto>> getAllPlayersByLevel(@PathVariable Integer level) {
        return new ResponseEntity<>(playerService.getAllPlayersByLevel(level), HttpStatus.OK);
    }

    @GetMapping("/profile-by-id/{id}")
    public ResponseEntity<PlayerInfoDto> getPlayerById(@PathVariable Integer id) {
        return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);
    }

    @GetMapping("/get-my-profile")
    public ResponseEntity<PlayerInfoDto> getMyProfile() {
        return new ResponseEntity<>(playerService.getMyProfile(), HttpStatus.OK);
    }

//    @GetMapping("/ingame-stats/{playerId}")
//    public ResponseEntity<PlayerStats> getPlayerStats(@PathVariable Integer playerId) {
//        return new ResponseEntity<PlayerStats>(playerService.getPlayerStatsByPlayerId(playerId), HttpStatus.OK);
//    }

    @PostMapping("/ingame-stats")
    public ResponseEntity<BasicMessageResponse> updateNewPlayerStats(@Valid @RequestBody RequestLearnNewPlayerStats newPlayerStats) {
        return new ResponseEntity<>(playerService.updateSkillPoints(newPlayerStats), HttpStatus.OK);
    }
}
