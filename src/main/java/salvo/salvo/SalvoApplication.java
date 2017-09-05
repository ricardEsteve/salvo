package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository,
                                      ScoreRepository scoreRepository) {
        return (String... args) -> {

            Player Jbauer = new Player("JBauer", "JBauer@example.com", "24");
            Player Cobrien = new Player("Cobrien", "Cobrien@example.com", "42");
            Player Kbauer = new Player("Kbauer", "Kbauer@example.com", "kb");
            Player Talmeida = new Player("Talmeida", "Talmeida@example.com", "mole");


            playerRepository.save(Jbauer);
            playerRepository.save(Cobrien);
            playerRepository.save(Kbauer);
            playerRepository.save(Talmeida);


            Date date = new Date();

            Game gameOne = new Game();
            Game gameTwo = new Game();
            gameTwo.addSeconds(3600);
            Game gameThree = new Game();
            gameThree.addSeconds(7200);

            gameRepository.save(gameOne);
            gameRepository.save(gameTwo);
            gameRepository.save(gameThree);

            GamePlayer gamePlayer1 = new GamePlayer(date, gameOne, Jbauer);
            GamePlayer gamePlayer2 = new GamePlayer(date, gameOne, Cobrien);
            GamePlayer gamePlayer3 = new GamePlayer(date, gameTwo, Kbauer);
            GamePlayer gamePlayer4 = new GamePlayer(date, gameTwo, Talmeida);
            GamePlayer gamePlayer5 = new GamePlayer(date, gameThree,Jbauer);



            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);
            gamePlayerRepository.save(gamePlayer5);



            Ship ship1 = new Ship(ShipType.patrolBoat, gamePlayer1, Arrays.asList("A1", "A2"));
            Ship ship2 = new Ship(ShipType.carrier, gamePlayer1, new ArrayList<>(Arrays.asList("C1","C2","C3","C4","C5")));
            Ship ship3 = new Ship(ShipType.submarine, gamePlayer2, new ArrayList<>(Arrays.asList("H2","H3","H4")));
            Ship ship4 = new Ship(ShipType.destroyer, gamePlayer2, new ArrayList<>(Arrays.asList("B2","B3","B4")));
            Ship ship5 = new Ship(ShipType.battleship, gamePlayer3, new ArrayList<>(Arrays.asList("C9","D9","E9","F9")));
            Ship ship6 = new Ship(ShipType.patrolBoat, gamePlayer3, new ArrayList<>(Arrays.asList("A6", "A7")));


            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);
            shipRepository.save(ship6);


            Salvo salvo1 = new Salvo(gamePlayer1, 1,new ArrayList<>(Arrays.asList("D5", "D6")));
            Salvo salvo2 = new Salvo(gamePlayer2, 1,new ArrayList<>(Arrays.asList("A1", "H5")));
            Salvo salvo3 = new Salvo(gamePlayer1, 2,new ArrayList<>(Arrays.asList("A1", "B1")));
            Salvo salvo4 = new Salvo(gamePlayer2, 2,new ArrayList<>(Arrays.asList("D3", "D4")));


            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);

            Score score1 = new Score(gameOne, Jbauer, new Date(), 1);

            Score score2= new Score(gameOne, Cobrien, new Date(), 0);

            Score score3= new Score(gameTwo, Kbauer, new Date(), 0.5);

            Score score4= new Score(gameTwo, Talmeida, new Date(), 0.5);


            scoreRepository.save(score1);
            scoreRepository.save(score2);
            scoreRepository.save(score3);
            scoreRepository.save(score4);




        };
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    private PlayerRepository playerRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override

            public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                Player player = playerRepository.findByEmail(name);
                if (player != null) {
                    return new User(player.getEmail(), player.getPassword(),
                            AuthorityUtils.createAuthorityList("USER"));
                } else {
                    throw new UsernameNotFoundException("Unknown user: " + name);
                }
            }
        };
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/games.css").permitAll()
                .antMatchers("/web/games.js").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/players").permitAll()
                .anyRequest().fullyAuthenticated();

        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
