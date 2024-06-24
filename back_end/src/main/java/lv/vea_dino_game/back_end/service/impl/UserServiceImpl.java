package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.exceptions.NoSuchUserException;
import lv.vea_dino_game.back_end.exceptions.ServiceCurrentlyUnavailableException;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.BooleanDto;
import lv.vea_dino_game.back_end.model.dto.DateTimeDto;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.model.dto.ManageUserDto;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailService;
import lv.vea_dino_game.back_end.service.IUserService;
import lv.vea_dino_game.back_end.service.helpers.EmailSenderService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// Controller and security-config set up so only the users with Admin role can have access
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepo userRepo;
    private final EmailSenderService emailService;
    private final Mapper mapper;


    @Override
    public BasicMessageResponse changeDisabledStatus(Integer id, BooleanDto dto) {
      if (id == null || id < 0)
        throw new NoSuchUserException("Invalid user id of " + id);
      User user = userRepo.findById(id).orElseThrow(() -> new NoSuchUserException("No user with the id of " + id));
      user.setAccountDisabled(dto.accountDisabled());
      userRepo.save(user);
      // emailService.sendEmail()
      return new BasicMessageResponse("User has been " + (dto.accountDisabled() ? "disabled." : "activated."));
    }

    @Override
    public BasicMessageResponse giveTempBan(Integer id, DateTimeDto info) {
      if (id == null || id < 0)
        throw new NoSuchUserException("Invalid user id of " + id);
        
      User user = userRepo.findById(id).orElseThrow(() -> new NoSuchUserException("No user with the id of " + id));
      
      if (user.getAccountDisabled()){
          return new BasicMessageResponse("User "+ user.getUsername()+ " is already permanently disabled");
      }
      user.setTempBanDateTime(info.tempBanDateTime());
      userRepo.save(user);
      // sendEmail
      return new BasicMessageResponse("User "+ user.getUsername()+ " has been successfully banned");
    }

    @Override
    public Integer getUserIdByUsername(String username) {
      if (username == null)
        throw new InvalidUserInputException("Invalid username: it cannot be null");
      User user = userRepo.findByUsername(username)
          .orElseThrow(() -> new InvalidUserInputException("No user with the username of " + username));
      return user.getId();
    }

    
    @Override
    public ManageUserDto getDetailedUserInfo(Integer id) {
      if (id == null || id < 0)
        throw new InvalidUserInputException("Invalid user id of " + id);
      User user = userRepo.findById(id)
          .orElseThrow(() -> new InvalidUserInputException("There is no user with the id of " + id));
      return mapper.userToManageUserDto(user);
    }
    
    


}
