package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;

import java.util.List;

public interface IAnnouncementService {

    BasicMessageResponse addAnnouncement(Announcement announcement);


    List<Announcement> getAnnouncementByUser(Integer userId);

    List<Announcement> getAnnouncementByClan(Integer clanId);


    BasicMessageResponse updateAnnouncementByAnnouncementId(Integer announcementId, Announcement upadatedAnnouncement);

    BasicMessageResponse deleteAnnouncement(Integer announcementId);
}
