package salvo.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository repository) {
        return (String... args) -> {
            repository.save(new Player("Albert", "albert@example.com"));
            repository.save(new Player("Anton", "Anton@example.com"));
            repository.save(new Player("Bruno", "Bruno@example.com"));
            repository.save(new Player("Roc", "Roc@example.com"));
            repository.save(new Player("Nuria", "Nuria@example.com"));

        };
    }
}