package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Announcement;

import java.util.List;

public interface IAnnouncementService {

    void addAnnouncement(Integer userId, Announcement announcement);


    List<Announcement> getAnnouncementByUser(Integer userId);

    List<Announcement> getAnnouncementByClan(Integer clanId);


    Announcement updateAnnouncementByAnnouncementId(Integer playerId, Integer announcementId, Announcement upadatedAnnouncement);

    Announcement deleteAnnouncement(Integer playerId, Integer announcementId);
}
