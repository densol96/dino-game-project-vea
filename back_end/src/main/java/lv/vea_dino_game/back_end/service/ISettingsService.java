package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.DescriptionDto;
import lv.vea_dino_game.back_end.model.dto.EmailDto;
import lv.vea_dino_game.back_end.model.dto.PasswordDto;
import lv.vea_dino_game.back_end.model.dto.UsernameDto;

public interface ISettingsService {
  BasicMessageResponse changeDescription(DescriptionDto data);

  BasicMessageResponse changeEmail(EmailDto data);

  BasicMessageResponse changeUsername(UsernameDto data);

  BasicMessageResponse changePassword(PasswordDto data);
}
