package lv.vea_dino_game.back_end.service;

import java.time.LocalDateTime;

import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.BooleanDto;
import lv.vea_dino_game.back_end.model.dto.DateTimeDto;
import lv.vea_dino_game.back_end.model.dto.ManageUserDto;

public interface IUserService {
    
    BasicMessageResponse changeDisabledStatus(Integer id, BooleanDto info);

    BasicMessageResponse giveTempBan(Integer id, DateTimeDto info);

    Integer getUserIdByUsername(String username);

    ManageUserDto getDetailedUserInfo(Integer id);
}
