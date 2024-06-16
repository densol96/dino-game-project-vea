package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidAnnouncementException;
import lv.vea_dino_game.back_end.exceptions.InvalidClanException;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.repo.IAnnouncementRepo;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IAnnouncementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements IAnnouncementService {

    private final IAnnouncementRepo announcementRepo;

    private final IPlayerRepo playerRepo;

    private final IClanRepo clanRepo;

    @Override
    public void addAnnouncement(Integer userId, Announcement announcement) {
        if (userId == null) {throw new InvalidPlayerException("userId is null");}
        Player player = playerRepo.findById(userId).get();
        if (player == null) {throw new InvalidPlayerException("Player does not exist");}
        Clan clan = clanRepo.findByPlayers(player);
        if (clan == null) {throw new InvalidClanException("User does not have a clan");}
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setTitle(announcement.getTitle());
        newAnnouncement.setContent(announcement.getContent());
        newAnnouncement.setDate();
        newAnnouncement.setClan(clan);
        newAnnouncement.setAuthor(player);
        announcementRepo.save(newAnnouncement);
    }

    @Override
    public List<Announcement> getAnnouncementByUser(Integer userId) {
        if (announcementRepo.count() == 0) {throw new InvalidAnnouncementException("No announcement");}
        if (userId < 0) {throw new InvalidPlayerException("Incorrect user id");}
        Player player = playerRepo.findById(userId).get();
        if (player == null) {throw new InvalidPlayerException("Player does not exist");}
        List<Announcement> announcement = announcementRepo.findAllByAuthor(player);
        if (announcement == null) {throw new InvalidAnnouncementException("No announcement with this author");}
        return announcement;
    }

    @Override
    public List<Announcement> getAnnouncementByClan(Integer clanId) {
        if (announcementRepo.count() == 0) {throw new InvalidAnnouncementException("No announcement");}
        if (clanId < 0) {throw new InvalidClanException("Incorrect clan id");}
        Clan clan = clanRepo.findById(clanId);
        if (clan == null) {throw new InvalidClanException("Clan with id " + " do not exist");}
        List<Announcement> announcement = announcementRepo.findAllByClan(clan);
        if (announcement == null) {throw new InvalidAnnouncementException("No announcement with this clan");}
        return announcement;
    }
}