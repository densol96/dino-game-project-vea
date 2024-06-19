package lv.vea_dino_game.back_end.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.ServiceCurrentlyUnavailableException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.CreateClanDto;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clans")
@RequiredArgsConstructor
public class ClanFilterController {

    private final IClanFilterService clanService;

    private final IPlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Clan>> getAllClans(){
      return new ResponseEntity<List<Clan>>(clanService.retrieveAll(), HttpStatus.OK);
    }

    @GetMapping("/minlevel/{level}")
    public ResponseEntity<List<Clan>> showClansByMinEntryLevel(@PathVariable("level") Integer level){
      return new ResponseEntity<List<Clan>>(clanService.retrieveAllByMinEntryLevel(level), HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Clan> getClanByID(@PathVariable("id") Integer id){

        return new ResponseEntity<Clan>(clanService.retrieveClanById(id), HttpStatus.OK);
    }

    @GetMapping("/sort-level-desc")
    public ResponseEntity<List<Clan>> getAllClansSortedByMinLevelDesc(){
        return new ResponseEntity<List<Clan>>(clanService.retrieveAllSortedByMinLevelDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-level-asc")
    public ResponseEntity<List<Clan>> getAllClansSortedByMinLevelAsc(){
        return new ResponseEntity<List<Clan>>(clanService.retrieveAllSortedByMinLevelAsc(), HttpStatus.OK);
    }

    @GetMapping("/sort-title-desc")
    public ResponseEntity<List<Clan>> getAllClansSortedByTitleDesc(){
        return new ResponseEntity<List<Clan>>(clanService.retrieveAllSortedByTitleDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-title-asc")
    public ResponseEntity<List<Clan>> getAllClansSortedByTitleAsc(){
        return new ResponseEntity<List<Clan>>(clanService.retrieveAllSortedByTitleAsc(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BasicMessageResponse> createClan(@Valid @RequestBody CreateClanDto clanDto) {
        return new ResponseEntity<BasicMessageResponse>(clanService.createClan(clanDto), HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<BasicMessageResponse> updateClan(@Valid @RequestBody CreateClanDto updatedClanDto){
        return new ResponseEntity<BasicMessageResponse>(clanService.updateClan(updatedClanDto), HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity<BasicMessageResponse> deleteClan() {
        return new ResponseEntity<BasicMessageResponse>(clanService.deleteClan(), HttpStatus.CREATED);
    }

    

}
