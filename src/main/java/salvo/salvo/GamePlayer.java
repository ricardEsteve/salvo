package salvo.salvo;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ricard Esteve on 17/07/2017.
 */


@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date joinDate;

    @JoinColumn(name = "game_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Game game;

    @JoinColumn(name = "player_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Player player;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();



    public GamePlayer () {

    }

    public GamePlayer (Date joinDate, Game newGame, Player newPlayer) {
        this.joinDate = joinDate;
        this.game = newGame;
        this.player = newPlayer;

    }

    public Date getJoinDate(){
        return this.joinDate;
    }

    public void setJoinDate(Date newDate){
        this.joinDate = newDate;
    }

    public Game getGame (){
        return this.game;
    }

    public void setGame(Game game){
        this.game= game;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void setPlayer( Player player){
        this.player= player;
    }

    public long getId() {
        return id;
    }


}
