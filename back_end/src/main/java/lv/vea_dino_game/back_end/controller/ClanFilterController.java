package lv.vea_dino_game.back_end.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.AllClanInfoViewDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.ClanDto;
import lv.vea_dino_game.back_end.model.dto.CreateClanDto;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clans")
@RequiredArgsConstructor
public class ClanFilterController {

    private final IClanFilterService clanService;

    private final IPlayerService playerService;

    @GetMapping
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClans(){
      return new ResponseEntity<List<AllClanInfoViewDto>>(clanService.retrieveAll(), HttpStatus.OK);
    }

    @GetMapping("/minlevel/{level}")
    public ResponseEntity<List<AllClanInfoViewDto>> showClansByMinEntryLevel(@PathVariable("level") Integer level){
      return new ResponseEntity<List<AllClanInfoViewDto>>(clanService.retrieveAllByMinEntryLevel(level), HttpStatus.OK);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ClanDto> getClanByID(@PathVariable("id") Integer id){
        return new ResponseEntity<ClanDto>(clanService.retrieveClanById(id), HttpStatus.OK);
    }

    @GetMapping("/sort-level-desc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByMinLevelDesc(){
        return new ResponseEntity<List<AllClanInfoViewDto>>(clanService.retrieveAllSortedByMinLevelDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-level-asc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByMinLevelAsc(){
        return new ResponseEntity<List<AllClanInfoViewDto>>(clanService.retrieveAllSortedByMinLevelAsc(), HttpStatus.OK);
    }

    @GetMapping("/sort-title-desc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByTitleDesc(){
        return new ResponseEntity<List<AllClanInfoViewDto>>(clanService.retrieveAllSortedByTitleDesc(), HttpStatus.OK);
    }

    @GetMapping("/sort-title-asc")
    public ResponseEntity<List<AllClanInfoViewDto>> getAllClansSortedByTitleAsc(){
        return new ResponseEntity<List<AllClanInfoViewDto>>(clanService.retrieveAllSortedByTitleAsc(), HttpStatus.OK);
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

    @GetMapping("/get-clan-with-me")
    public ResponseEntity<ClanDto> getClanWithMe(){
        return new ResponseEntity<ClanDto>(clanService.getClanWithMe(), HttpStatus.OK);
    }

    

}
