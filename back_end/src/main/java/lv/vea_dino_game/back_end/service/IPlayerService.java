package lv.vea_dino_game.back_end.service;

public interface IPlayerService {
    void joinClan(Integer playerId, Integer clanId);

    void enrollClan(Integer playerId);
}
