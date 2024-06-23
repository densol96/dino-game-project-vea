package lv.vea_dino_game.back_end.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.AllClanInfoViewDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.ClanDto;
import lv.vea_dino_game.back_end.model.dto.CreateClanDto;
import lv.vea_dino_game.back_end.service.IClanFilterService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/clans")
@RequiredArgsConstructor
public class ClanFilterController {

    private final IClanFilterService clanService;

    @GetMapping
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClans(){
      return new ResponseEntity<>(clanService.retrieveAll(), HttpStatus.OK);
    }

    @GetMapping("/minlevel/{level}")
    public ResponseEntity<List<AllClanInfoViewDto>> showClansByMinEntryLevel(@PathVariable("level") Integer level){
      return new ResponseEntity<>(clanService.retrieveAllByMinEntryLevel(level), HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ClanDto> getClanByID(@PathVariable("id") Integer id){
        return new ResponseEntity<>(clanService.retrieveClanById(id), HttpStatus.OK);
    }

    @GetMapping("/sort-level-desc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByMinLevelDesc(){
        return new ResponseEntity<>(clanService.retrieveAllSortedByMinLevelDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-level-asc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByMinLevelAsc(){
        return new ResponseEntity<>(clanService.retrieveAllSortedByMinLevelAsc(), HttpStatus.OK);
    }

    @GetMapping("/sort-title-desc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByTitleDesc(){
        return new ResponseEntity<>(clanService.retrieveAllSortedByTitleDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-title-asc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByTitleAsc(){
        return new ResponseEntity<>(clanService.retrieveAllSortedByTitleAsc(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BasicMessageResponse> createClan(@Valid @RequestBody CreateClanDto clanDto) {
        return new ResponseEntity<>(clanService.createClan(clanDto), HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<BasicMessageResponse> updateClan(@Valid @RequestBody CreateClanDto updatedClanDto){
        return new ResponseEntity<>(clanService.updateClan(updatedClanDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BasicMessageResponse> deleteClan() {
        return new ResponseEntity<>(clanService.deleteClan(), HttpStatus.CREATED);
    }

    @GetMapping("/get-clan-with-me")
    public ResponseEntity<ClanDto> getClanWithMe(){
        return new ResponseEntity<>(clanService.getClanWithMe(), HttpStatus.OK);
    }

    

}
