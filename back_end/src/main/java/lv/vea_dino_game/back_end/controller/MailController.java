package lv.vea_dino_game.back_end.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BasicMailDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.model.enums.SortByEnum;
import lv.vea_dino_game.back_end.service.IMailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/mail")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MailController {
  
  private final IMailService mailSerive;

  @PostMapping("/send")
  public ResponseEntity<BasicMessageResponse> sendMessage(@Valid @RequestBody MailDto dto) {
    return new ResponseEntity<>(mailSerive.sendMail(dto), HttpStatus.OK);
  }

  @GetMapping("/has-new-messages")
  public ResponseEntity<HasNewMessagesDto> hasAnyUnread() {
    return new ResponseEntity<>(mailSerive.hasUnreadMessages(), HttpStatus.OK);
  }

  @GetMapping("/get-all-incoming")
  public ResponseEntity<List<BasicMailDto>> getAllIncoming(@RequestParam("page") Integer page, @RequestParam("sortBy") String sortBy) {
    return new ResponseEntity<>(mailSerive.getAllIncomingMail(page, sortBy), HttpStatus.OK);
  }

  @GetMapping("/get-all-outgoing")
  public ResponseEntity<List<BasicMailDto>> getAllOutgoing(@RequestParam("page") Integer page) {
    return new ResponseEntity<>(mailSerive.getAllOutgoingMail(page), HttpStatus.OK);
  }

  @GetMapping("/get-number-pages-incoming")
  public ResponseEntity<Integer> getIncomingPagesNum(@RequestParam("sortBy") String sortBy) {
    return new ResponseEntity<>(mailSerive.getNumberOfPagesForAllIncomingMail(sortBy), HttpStatus.OK);
  }

  @GetMapping("/get-number-pages-outgoing")
  public ResponseEntity<Integer> getOutgoingPagesNum() {
    return new ResponseEntity<>(mailSerive.getNumberOfPagesForAllOutgoingMail(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BasicMailDto> getLetter(@PathVariable Integer id) {
    return new ResponseEntity<>(mailSerive.getMailById(id), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BasicMessageResponse> deleteLetter(@PathVariable Integer id) {
    return new ResponseEntity<>(mailSerive.removeMail(id), HttpStatus.OK);
  }
  
  
}
