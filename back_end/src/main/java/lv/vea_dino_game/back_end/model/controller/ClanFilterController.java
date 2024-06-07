package lv.vea_dino_game.back_end.model.controller;


import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Array;
import java.util.ArrayList;

@Controller
@RequestMapping("/clan/filter")
public class ClanFilterController {

    @Autowired
    private IClanFilterService clanService;

    @GetMapping("/show/all")
    public ResponseEntity getAllClans(){
        try{
            return new ResponseEntity<ArrayList<Clan>>(clanService.retriveAll(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/show/byMinLevel")
    public ResponseEntity showClansByMinEntryLevel(@PathVariable("level") int level){
        try {
            return new ResponseEntity<ArrayList<Clan>>(clanService.retriveAllByMinEntryLevel(level), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
