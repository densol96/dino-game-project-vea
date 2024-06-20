package lv.vea_dino_game.back_end.service.helpers;

import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.dto.*;
import org.springframework.stereotype.Service;

import lv.vea_dino_game.back_end.model.User;

@Service
public class Mapper {

  public UserMainDTO fromUserToDto(User user) {
    return new UserMainDTO(
      user.getId(),
      user.getUsername(),
      user.getEmail(),
      user.getPlayer().getClan() != null ? user.getPlayer().getClan().getTitle() : null,
      user.getPlayer().getPlayerStats(),
      user.getPlayer().getDinoType(),
      user.getPlayer().getLevel(),
      user.getPlayer().getExperience(),
      user.getPlayer().getLevel(),
      user.getPlayer().getDescription()
    );
    
  }

  public AllAnnouncementDto announcementToDto(Announcement announcement){
    return new AllAnnouncementDto(
      announcement.getId(),
      announcement.getAuthor().getUser().getUsername(),
      announcement.getTitle(),
      announcement.getContent(),
      announcement.getDate()
    );
  }

  public AllClanInfoViewDto convertToDto(Clan clan) {
    return new AllClanInfoViewDto(
            clan.getId(),
            clan.getTitle(),
            clan.getDescription(),
            clan.getDinoType(),
            clan.getAdmin().getUser().getUsername(),
            clan.getMinPlayerLevel(),
            clan.getMaxCapacity()
    );
  }

  public AllPlayerInfoDto playerToDto(Player player) {
    return new AllPlayerInfoDto(
            player.getId(),
            player.getUser().getUsername(),
            player.getDinoType(),
            player.getLevel()
    );
  }

  public ClanDto clanToDto(Clan clan) {
    return new ClanDto(
            clan.getId(),
            clan.getTitle(),
            clan.getDescription(),
            clan.getDinoType(),
            clan.getMaxCapacity(),
            clan.getMinPlayerLevel(),
            clan.getPlayers(),
            clan.getAdmin().getUser().getUsername()
    );
  }

  public PlayerInfoDto onePlayerToDto(Player player) {
    return new PlayerInfoDto(
            player.getId(),
            player.getUser().getUsername(),
            player.getDinoType(),
            player.getLevel(),
            player.getExperience(),
            player.getDescription()
    );
  }
}
