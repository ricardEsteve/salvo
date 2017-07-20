package salvo.salvo;

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.context.annotation.Bean;

        import java.util.Date;

@SpringBootApplication

public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
        return (String... args) -> {

            Player player1 = new Player("Albert", "albert@example.com");
            Player player2 = new Player ("Anton", "Anton@example.com");
            Player player3 = new Player ("Bruno", "Bruno@example.com");
            Player player4 = new Player("Roc", "Roc@example.com");
            Player player5 = new Player("Nuria", "Nuria@example.com");
            Player player6 = new Player("Ricard", "Ricard@example.com");

            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            playerRepository.save(player5);
            playerRepository.save(player6);

            Date data = new Date();

            Game gameOne = new Game();
            Game gameTwo = new Game();
            gameTwo.addSeconds(3600);
            Game gameThree = new Game();
            gameThree.addSeconds(7200);

            gameRepository.save(gameOne);
            gameRepository.save(gameTwo);
            gameRepository.save(gameThree);

            GamePlayer gamePlayer1 = new GamePlayer(data, gameOne, player1);
            GamePlayer gamePlayer2 = new GamePlayer(data, gameOne, player2);
            GamePlayer gamePlayer3 = new GamePlayer(data, gameTwo, player3);
            GamePlayer gamePlayer4 = new GamePlayer(data, gameTwo, player4);
            GamePlayer gamePlayer5 = new GamePlayer(data, gameThree, player5);
            GamePlayer gamePlayer6 = new GamePlayer(data, gameThree, player6);


            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);
            gamePlayerRepository.save(gamePlayer5);
            gamePlayerRepository.save(gamePlayer6);


        };
    }
}