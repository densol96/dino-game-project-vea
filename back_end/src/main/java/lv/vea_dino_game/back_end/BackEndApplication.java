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

import java.util.List;

@SpringBootApplication
public class BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

	@Bean
	public CommandLineRunner testDatabaseLayer(IClanRepo clanRepo, IPlayerRepo playerRepo){
      return (String... args) -> {
        
        Clan clanOne = new Clan("Carnivores", "We like meat and blood", 7,2);
        Clan clanTwo = new Clan("Herbivores", "We love peace and green", 20, 1);
        clanRepo.saveAll(List.of(clanOne, clanTwo));


        PlayerStats statsOne = new PlayerStats();
        PlayerStats statsTwo = new PlayerStats();

        Player playerOne = new Player(clanOne, statsOne, DinoType.carnivore);
        Player playerTwo = new Player(clanTwo, statsTwo, DinoType.herbivore);
        
        playerRepo.saveAll(List.of(playerOne, playerTwo)); //stats cascaded
      };
	}

}
