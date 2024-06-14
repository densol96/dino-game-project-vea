package lv.vea_dino_game.back_end.controller;


import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/clans")
public class ClanFilterController {

    @Autowired
    private IClanFilterService clanService;

    @GetMapping
    public ResponseEntity<List<Clan>> getAllClans(){
      return new ResponseEntity<List<Clan>>(clanService.retriveAll(), HttpStatus.OK);
    }

    @GetMapping("/minlevel/{level}")
    public ResponseEntity<List<Clan>> showClansByMinEntryLevel(@PathVariable("level") Integer level){
      return new ResponseEntity<List<Clan>>(clanService.retriveAllByMinEntryLevel(level), HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<Clan> getClanByID(@PathVariable("id") Integer id){

        return new ResponseEntity<Clan>(clanService.retriveClanById(id), HttpStatus.OK);
    }

    @GetMapping("/sort-level-desc")
    public ResponseEntity<List<Clan>> getAllClansSorteredByMinLevelDesc(){
        return new ResponseEntity<List<Clan>>(clanService.retriveAllSorteredByMinLevelDesc(), HttpStatus.OK);
    }

    @GetMapping("sort-level-asc")
    public ResponseEntity<List<Clan>> getAllClansSorteredByMinLevelAsc(){
        return new ResponseEntity<List<Clan>>(clanService.retriveAllSorteredByMinLevelAsc(), HttpStatus.OK);
    }

    @GetMapping("sort-title-desc")
    public ResponseEntity<List<Clan>> getAllClansSorteredByTitleDesc(){
        return new ResponseEntity<List<Clan>>(clanService.retriveAllSorteredByTitleDesc(), HttpStatus.OK);
    }

    @GetMapping("sort-title-asc")
    public ResponseEntity<List<Clan>> getAllClansSorteredByTitleAsc(){
        return new ResponseEntity<List<Clan>>(clanService.retriveAllSorteredByTitleAsc(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clan> updateClan(@PathVariable("id") Integer id, @RequestBody Clan updatedClan){
        Clan clan = clanService.updateClan(id, updatedClan);
        return new ResponseEntity<Clan>(clan, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Clan> deleteClan(@PathVariable("id") Integer id) {
        Clan deletedClan = clanService.deleteClan(id);
        return new ResponseEntity<>(deletedClan, HttpStatus.OK); 
    }

    

}
