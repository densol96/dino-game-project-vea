package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidAnnouncementException;
import lv.vea_dino_game.back_end.exceptions.InvalidClanException;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.repo.IAnnouncementRepo;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IAnnouncementService;
import lv.vea_dino_game.back_end.service.IAuthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements IAnnouncementService {

    private final IAnnouncementRepo announcementRepo;

    private final IPlayerRepo playerRepo;

    private final IClanRepo clanRepo;

    private final IAuthService authService;

    @Override
    public BasicMessageResponse addAnnouncement( Announcement announcement) {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Clan clan = clanRepo.findByPlayers(player);
        if (clan == null) {
            throw new InvalidClanException("User does not have a clan");
        }
        Announcement newAnnouncement = new Announcement(announcement.getTitle(), announcement.getContent(), clan, player);
        newAnnouncement.setDate();
        announcementRepo.save(newAnnouncement);
        return new BasicMessageResponse("Announcement has been successfully added to clan "+ clan.getTitle() + " !");
    }

    @Override
    public List<Announcement> getAnnouncementByUser(Integer userId) {
        if (announcementRepo.count() == 0) {
            throw new InvalidAnnouncementException("No announcement");
        }
        if (userId < 0) {
            throw new InvalidPlayerException("Incorrect user id");
        }
        Player player = playerRepo.findById(userId).orElseThrow(() -> new InvalidPlayerException("Player does not exist"));
        List<Announcement> announcement = announcementRepo.findAllByAuthor(player);
        if (announcement == null) {
            throw new InvalidAnnouncementException("No announcement with this author");
        }
        return announcement;
    }

    @Override
    public List<Announcement> getAnnouncementByClan(Integer clanId) {
        if (announcementRepo.count() == 0) {
            throw new InvalidAnnouncementException("No announcement");
        }
        if (clanId < 0) {
            throw new InvalidClanException("Incorrect clan id");
        }
        Clan clan = clanRepo.findById(clanId);
        if (clan == null) {
            throw new InvalidClanException("Clan with id " + " do not exist");
        }
        List<Announcement> announcement = announcementRepo.findAllByClan(clan);
        if (announcement == null) {
            throw new InvalidAnnouncementException("No announcement with this clan");
        }
        return announcement;
    }

    @Override
    public BasicMessageResponse updateAnnouncementByAnnouncementId(Integer announcementId, Announcement updatedAnnouncement) {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Integer playerId = player.getId();
        if (announcementId == null || announcementId < 0) {
            throw new InvalidAnnouncementException("Incorrect announcement id");
        }
        Announcement announcement = announcementRepo.findById(announcementId).orElseThrow(() -> new InvalidAnnouncementException("Announcement with id " + announcementId + " does not exist"));
        if (!announcement.getAuthor().getId().equals(playerId)) {
            throw new InvalidPlayerException("You have no right to update this announcement");
        }
        if (updatedAnnouncement.getTitle() != null) {
            announcement.setTitle(updatedAnnouncement.getTitle());
        }
        if (updatedAnnouncement.getContent() != null) {
            announcement.setContent(updatedAnnouncement.getContent());
        }
        if (updatedAnnouncement.getClan() != null) {
            announcement.setClan(updatedAnnouncement.getClan());
        }
        announcementRepo.save(announcement);
        return new BasicMessageResponse("Announcement with title" + announcement.getTitle() + " for clan "+ announcement.getClan().getTitle() + " updated");
    }

    @Override
    public BasicMessageResponse deleteAnnouncement(Integer announcementId) {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Integer playerId = player.getId();
        if (announcementId == null || announcementId < 0) {
            throw new InvalidAnnouncementException("Incorrect announcement ID");
        }
        Announcement announcement = announcementRepo.findById(announcementId).orElseThrow(() -> new InvalidAnnouncementException("Announcement with ID " + announcementId + " does not exist"));
        if (!announcement.getAuthor().getId().equals(playerId)) {
            throw new InvalidPlayerException("You have no right to delete this announcement");
        }
        announcementRepo.delete(announcement);
        return new BasicMessageResponse("Announcement with title "+ announcement.getTitle()+" for clan "+announcement.getClan().getTitle()+ " deleted");
    }


}

