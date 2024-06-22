package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.NoSuchUserException;
import lv.vea_dino_game.back_end.exceptions.ServiceCurrentlyUnavailableException;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.BanDto;
import lv.vea_dino_game.back_end.model.dto.BanWithTimeDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.MailDto;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailService;
import lv.vea_dino_game.back_end.service.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepo userRepo;
    private final IAuthService authService;
    private final IMailService mailService;


    @Override
    public BasicMessageResponse banUser(Integer id, BanDto info) {
        if (id < 0) throw new NoSuchUserException("User with this id = "+id+"id not correct id  must be positive");
        User user = userRepo.findById(id).orElseThrow(() -> new NoSuchUserException("User with this id = "+id+ " do not exist"));
        if (user.getAccountDisabled()){
            return new BasicMessageResponse("User "+ user.getUsername()+ " is already banned");
        }
        user.setAccountDisabled(true);
        userRepo.save(user);
        try {
            MailDto mail = new MailDto(user.getUsername(), info.title(), info.message());
            mailService.sendMail(mail);  // Uncommented this line
            userRepo.save(user);
        } catch (Exception e) {
            System.out.println("💥💥💥 Likely error sending email ---> " + e.getMessage());
            throw new ServiceCurrentlyUnavailableException(
                    "Mail is temporarily unavailable, please try again a bit later. If the problem persists, get in touch with the administrator of DinoConflict.");
        }
        return new BasicMessageResponse("User "+ user.getUsername()+ " has been successfully banned");
    }

    @Override
    public BasicMessageResponse unbanUser(Integer id) {
        if (id < 0) throw new NoSuchUserException("User with this id = "+id+"id not correct id  must be positive");
        User user = userRepo.findById(id).orElseThrow(() -> new NoSuchUserException("User with this id = "+id+ " do not exist"));
        if (!user.getAccountDisabled()){
            return new BasicMessageResponse("User "+ user.getUsername()+ "is not banned");
        }
        user.setAccountDisabled(false);
        userRepo.save(user);
        try {
            MailDto mail = new MailDto(user.getUsername(),"Unban", "You ban is removed");
            mailService.sendMail(mail);  // Uncommented this line
            userRepo.save(user);
        } catch (Exception e) {
            System.out.println("💥💥💥 Likely error sending email ---> " + e.getMessage());
            throw new ServiceCurrentlyUnavailableException(
                    "Mail is temporarily unavailable, please try again a bit later. If the problem persists, get in touch with the administrator of DinoConflict.");
        }
        return new BasicMessageResponse("User "+ user.getUsername()+ " has been successfully unbanned");
    }

    @Override
    public BasicMessageResponse banUserWithTime(Integer id, BanWithTimeDto info) {
        if (id < 0) throw new NoSuchUserException("User with this id = "+id+"id not correct id  must be positive");
        User user = userRepo.findById(id).orElseThrow(() -> new NoSuchUserException("User with this id = "+id+ " do not exist"));
        if (user.getAccountDisabled()){
            return new BasicMessageResponse("User "+ user.getUsername()+ " is already banned");
        }
        user.setAccountDisabled(true);
        if (info.date() == null || info.date().isBefore(LocalDateTime.now())){
            user.setTempBanDateTime(LocalDateTime.now().plusDays(2));
        }
        else {
            user.setTempBanDateTime(info.date());
        }

        userRepo.save(user);
        try {
            MailDto mail = new MailDto(user.getUsername(), info.title(), info.message());
            mailService.sendMail(mail);  // Uncommented this line
            userRepo.save(user);
        } catch (Exception e) {
            System.out.println("💥💥💥 Likely error sending email ---> " + e.getMessage());
            throw new ServiceCurrentlyUnavailableException(
                    "Mail is temporarily unavailable, please try again a bit later. If the problem persists, get in touch with the administrator of DinoConflict.");
        }
        return new BasicMessageResponse("User "+ user.getUsername()+ " has been successfully banned");
    }


}
