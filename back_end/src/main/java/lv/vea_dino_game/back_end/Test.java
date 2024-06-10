package lv.vea_dino_game.back_end;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
  public static void main(String[] args) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    String formattedDate = now.format(formatter);
    System.out.println(formattedDate);
  }
}
