package lv.vea_dino_game.back_end.controller;


import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clans")
@RequiredArgsConstructor
public class ClanFilterController {

    private final IClanFilterService clanService;

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
    public ResponseEntity<Clan> createClan(@RequestBody Clan clan){
        Clan newClan = clanService.createClan(clan);
        return new ResponseEntity<Clan>(newClan, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clan> updateClan(@PathVariable("id") Integer id, @RequestBody Clan updatedClan){
        Clan clan = clanService.updateClan(id, updatedClan);
        return new ResponseEntity<Clan>(clan, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Clan> deleteClan(@PathVariable("id") Integer id) {
        Clan deletedClan = clanService.deleteClan(id);
        return new ResponseEntity<Clan>(deletedClan, HttpStatus.OK);
    }

    

}
