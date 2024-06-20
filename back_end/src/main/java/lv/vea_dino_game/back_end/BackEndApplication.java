package lv.vea_dino_game.back_end;

import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Job;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.enums.DinoType;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IJobRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;

import lv.vea_dino_game.back_end.service.helpers.EmailSenderService;
import lv.vea_dino_game.back_end.service.impl.AuthServiceImpl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class BackEndApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackEndApplication.class, args);
    
  }

	@Bean
	public CommandLineRunner testDatabaseLayer(IClanRepo clanRepo, IPlayerRepo playerRepo, AuthServiceImpl service, IJobRepo jobRepo){
      return (String... args) -> {
        
        Clan clanOne = new Clan("Carnivores", "We like meat and blood", 7,1);
        Clan clanTwo = new Clan("Herbivores", "We love peace and green", 20, 1);
        clanRepo.saveAll(List.of(clanOne, clanTwo));


        PlayerStats statsOne = new PlayerStats();
        PlayerStats statsTwo = new PlayerStats();
        statsOne.setHealth(20);
        statsTwo.setHealth(20);
        statsOne.setArmor(20);
        statsTwo.setArmor(15);
        statsOne.setDamage(2);
        statsTwo.setDamage(3);
        statsOne.setAgility(20);
        statsTwo.setAgility(40);
        statsOne.setCriticalHitPercentage(100);
        statsTwo.setCriticalHitPercentage(30);

        Player playerOne = new Player(clanOne, statsOne, DinoType.carnivore);
        Player playerTwo = new Player(clanTwo, statsTwo, DinoType.herbivore);
        Player playerThree = new Player(null, null,DinoType.herbivore);
        Player player4 = new Player(null, null,DinoType.herbivore);
        player4.setLevel(3);

        Job job1 = new Job();
        job1.setHoursDuration(1);
        job1.setRewardCurrency(5);

        playerOne.setCurrentJob(job1);
        playerOne.setWorkingUntil(LocalDateTime.now().plusHours(job1.getHoursDuration()));

        playerRepo.saveAll(List.of(playerOne, playerTwo,playerThree, player4)); //stats cascaded

      };
  }
}
