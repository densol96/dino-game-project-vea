package lv.vea_dino_game.back_end;

import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.enums.DinoType;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.repo.IPlayerStats;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

	@Bean
	public CommandLineRunner testDatabaseLayer(IClanRepo clanRepo, IPlayerRepo playerRepo, IPlayerStats playerStatsRepo){
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				PlayerStats stats = new PlayerStats();
				playerStatsRepo.save(stats);
				PlayerStats stats1 = new PlayerStats();
				playerStatsRepo.save(stats1);
				Clan clan1 = new Clan("MyClan", "new clan that is good", 7,2);
				clanRepo.save(clan1);
				Player player1 = new Player(clan1, stats, DinoType.carnivore);
				playerRepo.save(player1);
				Player player2 = new Player(clan1, stats1, DinoType.herbivore);
				playerRepo.save(player2);
				Clan clan2 = new Clan("MyClan2", "new2 clan that is good", 20,1);
				clanRepo.save(clan2);
			}
		};
	}

}
