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
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String userName;
    private String email;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    public Player() { }

    public Player(String userNameInput, String emailInput) {
        this.userName = userNameInput;
        this.email = emailInput;
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
    public void setGamePlayers (Set<GamePlayer> gamePlayers){
    this.gamePlayers = gamePlayers;
    }

    public Set<GamePlayer> getGamePlayers(){
        return gamePlayers;
    }

    public void addGamePlayer (GamePlayer gamePlayer) {
        gamePlayers.add(gamePlayer);
    }

}



