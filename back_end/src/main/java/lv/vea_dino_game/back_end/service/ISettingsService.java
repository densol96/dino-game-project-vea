package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.DescriptionDto;
import lv.vea_dino_game.back_end.model.dto.EmailDto;
import lv.vea_dino_game.back_end.model.dto.PasswordDto;
import lv.vea_dino_game.back_end.model.dto.ReducedPasswordDto;
import lv.vea_dino_game.back_end.model.dto.UsernameDto;

public interface ISettingsService {
  BasicMessageResponse changeDescription(DescriptionDto data, Integer id);

  BasicMessageResponse changeEmail(EmailDto data, Integer id);

  BasicMessageResponse changeUsername(UsernameDto data, Integer id);

  BasicMessageResponse changePassword(PasswordDto data);

  BasicMessageResponse changePassword(ReducedPasswordDto data, Integer id);
}
