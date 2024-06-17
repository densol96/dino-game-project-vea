package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.repo.ICombatRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.ICombatService;
import lv.vea_dino_game.back_end.service.helpers.CombatServiceHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CombatServiceImpl implements ICombatService {

    private final ICombatRepo combatRepo;
    private final IPlayerRepo playerRepo;
    private final CombatServiceHelper combatServiceHelper;


    @Override
    public Combat attackSelectedPlayerOnArena(Integer attackerId, Integer defenderId) {
        if (attackerId == null || !playerRepo.existsById(attackerId)) throw new InvalidPlayerException("attackerId " + attackerId + " does not exist");
        if (defenderId == null || !playerRepo.existsById(defenderId)) throw new InvalidPlayerException("defenderId " + defenderId + " does not exist");
        Player attacker = playerRepo.findById(attackerId).get();
        Player defender = playerRepo.findById(defenderId).get();

        return combatServiceHelper.simulateCombat(attacker, defender);
    }
}
