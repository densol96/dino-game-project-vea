package lv.vea_dino_game.back_end.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class TestController {

  @GetMapping("api/v1/test")
  public ResponseEntity<String> test() {
    return new ResponseEntity<String>("HEllo", HttpStatus.OK);
  }
}
