package lv.vea_dino_game.back_end.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.NoSuchUserException;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailSerive;
import lv.vea_dino_game.back_end.service.helpers.Mapper;

@Service
@RequiredArgsConstructor
public class MailSeriveImpl implements IMailSerive {
  
  private final IAuthService authService;
  private final Mapper mapper;
  private final IUserRepo userRepo;

  @Override
  public BasicMessageResponse sendMail(MailDto dto) {
    if (!userRepo.existsByUsername(dto.to()))
      throw new NoSuchUserException("Unable to send mail as no such user with username of " + dto.to());
    return new BasicMessageResponse("Your message has been sent");
  }
  
}
