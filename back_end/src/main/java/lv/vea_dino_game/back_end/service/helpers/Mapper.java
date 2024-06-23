package lv.vea_dino_game.back_end.service.helpers;

import lv.vea_dino_game.back_end.model.*;
import lv.vea_dino_game.back_end.model.dto.*;
import org.springframework.stereotype.Service;

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
        user.getPlayer().getCurrency(),
        user.getPlayer().getDescription(),
        user.getPlayer().getCombatStats(),
        user.getPlayer().getCurrentJob(),
        user.getPlayer().getImmuneUntil(),
        user.getPlayer().getWorkingUntil(),
        user.getPlayer().getCannotAttackAgainUntil()
      );
  }

  public PlayerForRatingsDto fromPlayerToForRatingsDto(Player player) {
    return new PlayerForRatingsDto(
          player.getUser().getId(),
          player.getUser().getUsername(),
          player.getDinoType(),
          player.getExperience(),
          player.getCombatStats().getCombatsTotal(),
          player.getCombatStats().getCombatsWon(),
          player.getCombatStats().getCurrencyWon());
  }
  
   public PublicUserDto fromUserToPublicDto(User user) {
    return new PublicUserDto(
      user.getId(),
      user.getUsername(),
      user.getPlayer().getClan() != null ? user.getPlayer().getClan().getTitle() : null,
      user.getPlayer().getPlayerStats(),
      user.getPlayer().getDinoType(),
      user.getPlayer().getLevel(),
      user.getPlayer().getExperience(),
      user.getPlayer().getDescription(),
      user.getPlayer().getCombatStats()
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

  public FriendDto oneFriendToDto(Friend friend) {
    return new FriendDto(
            friend.getId(),
            friend.getFriend().getUser().getUsername(),
            friend.getFriend().getDinoType(),
            friend.getFriend().getLevel(),
            friend.getPlayer().getUser().getUsername(),
            friend.getPlayer().getDinoType(),
            friend.getPlayer().getLevel()
    );
  }

    public NewsDto fromNewsToNewsDto(News news) {
    return new NewsDto(
            news.getId(),
            news.getTitle(),
            news.getContent(),
            news.getDate()
    );
    }
}
