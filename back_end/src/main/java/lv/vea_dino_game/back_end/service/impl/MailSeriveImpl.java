package lv.vea_dino_game.back_end.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.exceptions.NoSuchUserException;
import lv.vea_dino_game.back_end.exceptions.ServiceCurrentlyUnavailableException;
import lv.vea_dino_game.back_end.model.dto.BasicMailDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.HasNewMessagesDto;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.model.enums.MailType;
import lv.vea_dino_game.back_end.model.enums.MailFilterByEnum;
import lv.vea_dino_game.back_end.model.MailMessage;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.UserMailMessage;
import lv.vea_dino_game.back_end.repo.IMailMessageRepo;
import lv.vea_dino_game.back_end.repo.IUserMailMessageRepo;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;

@Service
@RequiredArgsConstructor
public class MailSeriveImpl implements IMailService {
  
  private final static Integer RESULTS_PER_PAGE = 5;

  private final IAuthService authService;
  private final Mapper mapper;
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
    User to = userRepo.findByUsername(dto.to()).orElseThrow(() -> new InvalidUserInputException("User does not exist"));
    
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

  private List<BasicMailDto> getAllMail(Integer page, MailType type, MailFilterByEnum sortBy) {
    if (page == null || page < 1)
      throw new InvalidUserInputException("Invalid user input for the page of " + page);

    /*
     * At first, thought to throw an exception, but actually, in order for front-end to work properly, need to return [] here.
     */
    // Integer upperLimitForPage = type == MailType.FROM ? getNumberOfPagesForAllOutgoingMail()
    //     : getNumberOfPagesForAllIncomingMail(sortBy.toString());
      
    // With Pageable, pageNumber starts at 0 => 1 page is at 0 index, 2 at 1 etc. 
    Pageable pageable = PageRequest.of(page - 1, RESULTS_PER_PAGE, Sort.by("mail.sentAt").descending());

    List<UserMailMessage> userMailList = null;

    if(sortBy == MailFilterByEnum.ALL) {
      userMailList = userMailMessageRepo
          .findAllByUserUsernameAndType(authService.getLoggedInUser().getUsername(), type, pageable);
    } else {
      userMailList = userMailMessageRepo
          .findAllByUserUsernameAndTypeAndIsUnread(authService.getLoggedInUser().getUsername(), type, pageable, sortBy == MailFilterByEnum.UNREAD ? true : false);
    }
    // could check if the list size is 0 here, but I want to send an empty array back, BUT if any other exception to throw an exception(will make it easier to consume such API on React side)
    if (userMailList == null) {
      System.out.println("SERVER EEROR LOG ----> check MailSeriveImpl.getAllMail");
      throw new ServiceCurrentlyUnavailableException("Soory, but displaying your mail is cirrently unavailable");
    }
    return userMailList.stream().map((userMail) -> mapper.userMailMessageToBasicDto(userMail)).toList();
  }
  
  @Override
  public List<BasicMailDto> getAllIncomingMail(Integer page, String sortByValue) {
    MailFilterByEnum sortBy = processSortByValueToEnum(sortByValue);
    return getAllMail(page, MailType.TO, sortBy);
  }

  @Override
  public List<BasicMailDto> getAllOutgoingMail(Integer page) {
    return getAllMail(page, MailType.FROM, MailFilterByEnum.ALL);
  }

  private MailFilterByEnum processSortByValueToEnum(String sortByString) {
    try {
       return MailFilterByEnum.valueOf(sortByString.toUpperCase());
     } catch (Exception e) {
       throw new InvalidUserInputException("Invalid sortBy(all, read, unread) value of " + sortByString);
    }
  }
  

  @Override
  public Integer getNumberOfPagesForAllIncomingMail(String sortByValue) {

    MailFilterByEnum sortBy = processSortByValueToEnum(sortByValue);

    Integer resultsTotal;

    if(sortBy == MailFilterByEnum.ALL) {
      resultsTotal = userMailMessageRepo.countByUserUsernameAndType(authService.getLoggedInUser().getUsername(),
          MailType.TO);
    }
    else if(sortBy == MailFilterByEnum.READ) {
      resultsTotal = userMailMessageRepo.countByUserUsernameAndTypeAndIsUnread(authService.getLoggedInUser().getUsername(),
          MailType.TO, false);
    } else {
      resultsTotal = userMailMessageRepo.countByUserUsernameAndTypeAndIsUnread(authService.getLoggedInUser().getUsername(),
          MailType.TO, true);
    }
  
    return (int) Math.ceil((double) resultsTotal / RESULTS_PER_PAGE);
  }

  @Override
  public Integer getNumberOfPagesForAllOutgoingMail() {
    Integer resultsTotal = userMailMessageRepo.countByUserUsernameAndType(authService.getLoggedInUser().getUsername(), MailType.FROM);
    return resultsTotal == 0 ? 0 : (int) Math.ceil((double) resultsTotal / RESULTS_PER_PAGE);
  }

  @Override
  public BasicMailDto getMailById(Integer id) {
    if(id == null || id < 0 || !userMailMessageRepo.existsById(id))
      throw new InvalidUserInputException("Sorry, but the provided mail's id is invalid --> " + id);
    
    UserMailMessage userMail = userMailMessageRepo.findById(id).get();
    // mark as read at this point
    userMail.setIsUnread(false);
    userMailMessageRepo.save(userMail);
    return mapper.userMailMessageToBasicDto(userMail);
  }

  // @Override
  // public BasicMessageResponse removeMail(Integer id) {
  //   if (id == null || id < 0 || !userMailMessageRepo.existsById(id))
  //     throw new InvalidUserInputException("Sorry, but the provided mail's id is invalid --> " + id);
  //   userMailMessageRepo.deleteById(id);
  //   return new BasicMessageResponse("Letter has been successfully deleted");
  // }

  @Override
  public BasicMessageResponse removeMail(Integer id) {
    if (id == null || id < 0 || !userMailMessageRepo.existsById(id))
      throw new InvalidUserInputException("Sorry, but the provided mail's id is invalid --> " + id);
    UserMailMessage userMailCopy = userMailMessageRepo.findById(id).get();
    MailMessage original = userMailCopy.getMail();
    /*
     * First feel is that this is redundant, but actually without SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP 
     * this aint gonna work (killed some time debugging this and reading about unexpected behavior 
     * that occurs when you have bidirectional relationship and you're not synchronizing both sides WHILE having both 
     * parent and child persisted (attached to the current session)
     */
    original.getUserMailMessages().remove(userMailCopy);
    userMailCopy.setMail(null);
    userMailMessageRepo.deleteById(id);
    if (original.getUserMailMessages().size() == 1) {
      mailMessageRepo.save(original); 
    } else {
      mailMessageRepo.delete(original);
    }
    
    return new BasicMessageResponse("Letter has been successfully deleted");
  }
  
  @Override
  public void sendNotificationFromAdmin(String usernameTo, String title, String text) {
    User admin = userRepo.findByUsername("admin").orElseThrow(() -> new ServiceCurrentlyUnavailableException("The notifications service is currently unavailable"));
    User to = userRepo.findByUsername(usernameTo)
        .orElseThrow(() -> new InvalidUserInputException("No such user with username of " + usernameTo));
    MailMessage mail = new MailMessage();
    mail.setFrom(admin);
    mail.setTo(to);
    mail.setTitle(title);
    mail.setMessageText(text);

    UserMailMessage fromMessage = new UserMailMessage();
    fromMessage.setMail(mail);
    fromMessage.setType(MailType.FROM);
    fromMessage.setUser(admin);


    UserMailMessage toMessage = new UserMailMessage();
    toMessage.setMail(mail);
    toMessage.setType(MailType.TO);
    toMessage.setUser(to);
    toMessage.setIsUnread(true);

    mailMessageRepo.save(mail);
    userMailMessageRepo.saveAll(List.of(fromMessage, toMessage));
  }
}
