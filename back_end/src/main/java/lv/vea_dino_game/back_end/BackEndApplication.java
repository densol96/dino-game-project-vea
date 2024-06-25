package lv.vea_dino_game.back_end;

import lv.vea_dino_game.back_end.model.Clan;



import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.enums.DinoType;
import lv.vea_dino_game.back_end.model.enums.Role;
import lv.vea_dino_game.back_end.repo.IClanRepo;


import lv.vea_dino_game.back_end.repo.IUserRepo;



import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@SpringBootApplication
public class BackEndApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackEndApplication.class, args);
    
  }

	// @Bean
  //   public CommandLineRunner testDatabaseLayer(IClanRepo clanRepo, IPlayerRepo playerRepo, AuthServiceImpl service) {
  //     return (String... args) -> {

  //       Clan clanOne = new Clan("Carnivores", "We like meat and blood", 7, 1);
  //       Clan clanTwo = new Clan("Herbivores", "We love peace and green", 20, 1);
  //       clanRepo.saveAll(List.of(clanOne, clanTwo));

  //       PlayerStats statsOne = new PlayerStats();
  //       PlayerStats statsTwo = new PlayerStats();
  //       statsOne.setHealth(20);
  //       statsTwo.setHealth(20);
  //       statsOne.setArmor(20);
  //       statsTwo.setArmor(15);
  //       statsOne.setDamage(2);
  //       statsTwo.setDamage(3);
  //       statsOne.setAgility(20);
  //       statsTwo.setAgility(40);
  //       statsOne.setCriticalHitPercentage(100);
  //       statsTwo.setCriticalHitPercentage(30);

  //       Player playerOne = new Player(clanOne, statsOne, DinoType.carnivore);
  //       Player playerTwo = new Player(clanTwo, statsTwo, DinoType.herbivore);
  //       Player playerThree = new Player(null, null, DinoType.herbivore);
  //       Player player4 = new Player(null, null, DinoType.herbivore);
  //       player4.setLevel(3);

  //       playerRepo.saveAll(List.of(playerOne, playerTwo, playerThree, player4)); //stats cascaded
  //     };
  //   }

  @Bean
  public CommandLineRunner createDefaultPlayers(IClanRepo clanRepo, IUserRepo userRepo, PasswordEncoder encoder) {
    return (String... args) -> {


     User admin = new User("admin", "admin@admin.com", encoder.encode("admin"), Role.ADMIN, DinoType.carnivore, null, 20, "I am the admin");

      User u1 = new User("solodeni", "solo@deni.com", encoder.encode("password123"), Role.ADMIN, DinoType.carnivore, null,
          10, "I am solodeni");
      User u2 = new User("davidka", "solo@davi.com", encoder.encode("pasword123"), Role.USER, DinoType.carnivore, null,
          10, "I am davidka");
      u2.getPlayer().setLevel(2);
      User u3 = new User("mihails", "mihails@mihails.com", encoder.encode("pasword123"), Role.USER, DinoType.carnivore, null,
          5, "I am mihails");
      User u4 = new User("daniels", "daniels@daniels.com", encoder.encode("pasword123"), Role.USER, DinoType.carnivore, null,
          1, "I am daniels");
      User u5 = new User("janis", "janis@deni.com", encoder.encode("pasword123"), Role.USER, DinoType.carnivore, null,
          3, null);
      User u6 = new User("peteris", "peteris@deni.com", encoder.encode("pasword123"), Role.USER, DinoType.carnivore, null,
          20, null);
      User u7 = new User("rihards", "rihards@deni.com", encoder.encode("pasword123"), Role.USER, DinoType.herbivore, null,
          20, null);
      User u8 = new User("naruto", "solo@naruto.com", encoder.encode("pasword123"), Role.USER, DinoType.herbivore, null,
          17, null);
      User u9 = new User("sasuke", "sasuke@deni.com", encoder.encode("pasword123"), Role.USER, DinoType.herbivore, null,
          18, null);
      User u10 = new User("itachi", "itachi@deni.com", encoder.encode("pasword123"), Role.USER, DinoType.herbivore,
          null, 14, null);

      userRepo.saveAll(List.of(u1, u2, u3, u4, u5, u6, u7, u8, u9, u10, admin));
    };
  }
}
