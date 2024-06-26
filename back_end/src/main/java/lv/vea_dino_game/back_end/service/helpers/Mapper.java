package lv.vea_dino_game.back_end.service.helpers;

import lv.vea_dino_game.back_end.model.*;
import lv.vea_dino_game.back_end.model.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        user.getPlayer().getCannotAttackAgainUntil(),
        user.getPlayer().getClan() != null ? user.getPlayer().getClan().getId() : null,
        user.getRole()
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
      user.getPlayer().getCombatStats(),
      user.getRole()
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
            clan.getMaxCapacity(),
            clan.getPlayers().size()
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
    List<String> usernames = clan.getPlayers().stream()
            .map(player -> player.getUser().getUsername())
            .collect(Collectors.toList());
    List<AllPlayerInfoDto> players = clan.getPlayers().stream()
            .map(player -> this.playerToDto(player))
            .collect(Collectors.toList());

    return new ClanDto(
            clan.getId(),
            clan.getTitle(),
            clan.getDescription(),
            clan.getDinoType(),
            clan.getMaxCapacity(),
            clan.getMinPlayerLevel(),
            players,
            usernames,
            clan.getAdmin().getUser().getUsername(),
            clan.getAdmin().getId()
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
            friend.getPlayer().getId(),
            friend.getFriend().getId(),
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
        news.getDate());
  }
  
  public ManageUserDto userToManageUserDto(User user) {
    return new ManageUserDto(
        user.getId(),
        user.getPlayer().getDescription(),
        user.getEmail(),
        user.getUsername(),
        user.getRegistrationDate(),
        user.getLastLoggedIn(),
        user.getIsEmailConfirmed(),
        user.getTempBanDateTime(),
        user.getAccountDisabled());
  }
  
  public BasicMailDto userMailMessageToBasicDto (UserMailMessage userMail) {
    MailMessage actualMail = userMail.getMail();
    return new BasicMailDto(
      userMail.getId(),
      actualMail.getFrom().getUsername(),
      actualMail.getTo().getUsername(),
      actualMail.getTitle(),
      actualMail.getMessageText(),
      actualMail.getSentAt(),
      userMail.getIsUnread(),
      userMail.getType()
      );
  }
}
