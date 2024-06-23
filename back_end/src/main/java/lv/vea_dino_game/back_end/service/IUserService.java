package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.dto.BanDto;
import lv.vea_dino_game.back_end.model.dto.BanWithTimeDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;

public interface IUserService {
    BasicMessageResponse banUser(Integer id, BanDto info);

    BasicMessageResponse unbanUser(Integer id);

    BasicMessageResponse banUserWithTime(Integer id, BanWithTimeDto info);

    Integer getUserIdByUsername(String username);
}
