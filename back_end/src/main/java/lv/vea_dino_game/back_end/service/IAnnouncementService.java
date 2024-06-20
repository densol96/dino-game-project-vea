package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.dto.AllAnnouncementDto;
import lv.vea_dino_game.back_end.model.dto.AnnouncementDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;

import java.util.List;

public interface IAnnouncementService {

    BasicMessageResponse addAnnouncement(AnnouncementDto announcementDto);


    List<AllAnnouncementDto> getAnnouncementByUser(Integer userId);

    List<AllAnnouncementDto> getAnnouncementByClan(Integer clanId);


    BasicMessageResponse updateAnnouncementByAnnouncementId(Integer announcementId, AnnouncementDto upadatedAnnouncementDto);

    BasicMessageResponse deleteAnnouncement(Integer announcementId);
}
