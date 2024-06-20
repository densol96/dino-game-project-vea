package lv.vea_dino_game.back_end.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.service.IMailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/mail")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MailController {
  
  private final IMailService mailSerive;

  @PostMapping("/send")
  public ResponseEntity<BasicMessageResponse> sendMessage(@Valid @RequestBody MailDto dto) {
    return new ResponseEntity<BasicMessageResponse>(mailSerive.sendMail(dto), HttpStatus.OK);
  }

  @GetMapping("/has-new-messages")
  public ResponseEntity<HasNewMessagesDto> getMethodName() {
    return new ResponseEntity<>(mailSerive.hasUnreadMessages(), HttpStatus.OK);
  }
  
  
}
