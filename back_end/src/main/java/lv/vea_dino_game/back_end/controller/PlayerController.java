package lv.vea_dino_game.back_end.controller;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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




}
