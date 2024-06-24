package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Job;
import lv.vea_dino_game.back_end.model.PlayerCombatsStats;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.enums.DinoType;
import lv.vea_dino_game.back_end.model.enums.Role;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record UserMainDTO(
  Integer id,
  String username,
  String email,
  String clanTitle,
  PlayerStats playerStats,
  DinoType dinoType,
  Integer level,
  Integer experience,
  Integer currency,
  String description,
  PlayerCombatsStats combatStats,
  Job currentJob,
  LocalDateTime immuneUntil,
  LocalDateTime workingUntil,
  LocalDateTime cannotAttackAgainUntil,
  Integer clanId,
  Role role
) {}
