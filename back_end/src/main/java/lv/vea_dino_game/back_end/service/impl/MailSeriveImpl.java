package lv.vea_dino_game.back_end.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.exceptions.NoSuchUserException;
import lv.vea_dino_game.back_end.model.dto.BasicMailDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.model.enums.MailType;
import lv.vea_dino_game.back_end.model.MailMessage;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.UserMailMessage;
import lv.vea_dino_game.back_end.repo.IMailMessageRepo;
import lv.vea_dino_game.back_end.repo.IUserMailMessageRepo;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailService;

@Service
@RequiredArgsConstructor
public class MailSeriveImpl implements IMailService {
  
  private final static Integer RESULTS_PER_PAGE = 5;

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

    // if(dto.to().equals(from.getUsername()))
    //   throw new NoSuchUserException("You are not meant to send mail to yourself");
    
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

  @Override
  public HasNewMessagesDto hasUnreadMessages() {
    User user = authService.getLoggedInUser();
    return new HasNewMessagesDto(userMailMessageRepo.existsByUserIdAndIsUnread(user.getId(), true));
  }

  private List<BasicMailDto> getAllMail(Integer page, MailType type) {
    if (page == null || page < 1 || page > 1000)
      throw new InvalidUserInputException("Invalid user input for the page of " + page);
    // With Pageable, pageNumber starts at 0 => 1 page is at 0 index, 2 at 1 etc. 
    Pageable pageable = PageRequest.of(page - 1, RESULTS_PER_PAGE, Sort.by("mail.sentAt").descending());
    List<UserMailMessage> userMailList = userMailMessageRepo
        .findAllByUserUsernameAndType(authService.getLoggedInUser().getUsername(), type, pageable);
    // could check if the list size is 0 here, but I want to send an empty array back, BUT if any other exception to throw an exception(will make it easier to consume such API on React side)
    return userMailList.stream().map((userMail) -> {
      var actualMail = userMail.getMail();
      return new BasicMailDto(
          userMail.getId(),
          actualMail.getFrom().getUsername(),
          actualMail.getTo().getUsername(),
          actualMail.getTitle(),
          actualMail.getMessageText(),
          actualMail.getSentAt());
    }).toList();
  }
  
  @Override
  public List<BasicMailDto> getAllIncomingMail(Integer page) {
    return getAllMail(page, MailType.TO);
  }

  @Override
  public List<BasicMailDto> getAllOutgoingMail(Integer page) {
    return getAllMail(page, MailType.FROM);
  }
  

  @Override
  public Integer getNumberOfPagesForAllIncomingMail() {
    Integer resultsTotal = userMailMessageRepo.countByUserUsernameAndType(authService.getLoggedInUser().getUsername(),
        MailType.TO);
    return (int) Math.ceil((double) resultsTotal / RESULTS_PER_PAGE);
  }

  @Override
  public Integer getNumberOfPagesForAllOutgoingMail() {
    Integer resultsTotal = userMailMessageRepo.countByUserUsernameAndType(authService.getLoggedInUser().getUsername(), MailType.FROM);
    return (int) Math.ceil((double) resultsTotal / RESULTS_PER_PAGE);
  }
  
}