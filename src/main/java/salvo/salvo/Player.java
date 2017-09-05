package salvo.salvo;

/**
 * Created by Ricard Esteve on 11/07/2017.
 */



import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String userName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    public Player() {
    }

    public Player(String userNameInput, String emailInput, String password) {
        this.userName = userNameInput;
        this.email = emailInput;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return userName + " " + email;
    }

    public long getId() {
        return id;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
    }

    public Score getScore(Game anyGame) {

        for (Score score : scores) {
            if (score.getGame().equals(anyGame)) {
                return score;
            }
        }
        return null;

        //return scores.stream().filter(score -> score.getGame().equals(anyGame)).findFirst().orElse(null);
    }

}


//$.post("/api/login", { name: "JBauer@example.com", pwd: "24" }).done(function() { console.log("logged in!"); }).fail(function() { console.log("log in failed!"); })

//$.post("/api/logout", null).done(function() { console.log("logged out!"); }).fail(function() { console.log("log out failed!"); })