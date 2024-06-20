package lv.vea_dino_game.back_end.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.NoSuchUserException;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.model.enums.MailType;
import lv.vea_dino_game.back_end.model.MailMessage;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.UserMailMessage;
import lv.vea_dino_game.back_end.repo.IMailMessageRepo;
import lv.vea_dino_game.back_end.repo.IUserMailMessageRepo;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailSerive;
import lv.vea_dino_game.back_end.service.helpers.Mapper;

@Service
@RequiredArgsConstructor
public class MailSeriveImpl implements IMailSerive {
  
  private final IAuthService authService;
  // private final Mapper mapper;
  private final IUserRepo userRepo;
  private final IMailMessageRepo mailMessageRepo;
  private final IUserMailMessageRepo userMailMessageRepo;

  @Override
  public BasicMessageResponse sendMail(MailDto dto) {
    if (!userRepo.existsByUsername(dto.to()))
      throw new NoSuchUserException("Unable to send mail as no such user with username of " + dto.to());
    User from = authService.getLoggedInUser();

    if(dto.to().equals(from.getUsername()))
      throw new NoSuchUserException("You are not meant to send mail to yourself");
    
    // At this point, we are sure there is such user
    User to = userRepo.findByUsername(dto.to()).get();
    
    // Create a message
    MailMessage mail = new MailMessage();
    mail.setFrom(from);
    mail.setTo(to);
    mail.setTitle(dto.title());
    mail.setMessageText(dto.messageText());
    
    /*
     * Each user will have an asssociated entity to this message (so, if for example, from-user deletes the message, he will just remove the 
     * link to the original message and to-user will not lose the access)
     */
    UserMailMessage fromMessage = new UserMailMessage();
    fromMessage.setMail(mail);
    fromMessage.setType(MailType.FROM);
    fromMessage.setUser(from);


    UserMailMessage toMessage = new UserMailMessage();
    toMessage.setMail(mail);
    toMessage.setType(MailType.TO);
    toMessage.setUser(to);
    toMessage.setIsUnread(true);


    mailMessageRepo.save(mail);
    userMailMessageRepo.saveAll(List.of(fromMessage, toMessage));

    return new BasicMessageResponse("Your message has been sent");
  }
  
}
