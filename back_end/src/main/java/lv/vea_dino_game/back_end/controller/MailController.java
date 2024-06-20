package lv.vea_dino_game.back_end.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.service.IMailSerive;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/mail")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MailController {
  
  private final IMailSerive mailSerive;

  @PostMapping("/send")
  public ResponseEntity<BasicMessageResponse> sendMessage(@Valid @RequestBody MailDto dto) {
    return new ResponseEntity<BasicMessageResponse>(mailSerive.sendMail(dto), HttpStatus.OK);
  }
  
}
