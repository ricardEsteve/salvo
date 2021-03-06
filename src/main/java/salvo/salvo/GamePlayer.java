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

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvos = new HashSet<>();

    public GamePlayer() {
    }

    public GamePlayer(Date joinDate, Game newGame, Player newPlayer) {
        this.joinDate = joinDate;
        this.game = newGame;
        // Avisar a newGame que ESTE GamePlayer es suyo tambien
        newGame.addGamePlayer(this);
        this.player = newPlayer;
        // Avisar a newPlayer que ESTE GamePlayer es suyo tambien
        newPlayer.addGamePlayer(this);
    }


    public Date getJoinDate() {
        return this.joinDate;
    }

    public void setJoinDate(Date newDate) {
        this.joinDate = newDate;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public Set<Ship> getShips() {
        return this.ships;
    }

    public void addShips(Ship ship) {
        ships.add(ship);
    }

    public Set<Salvo> getSalvo() {
        return salvos;
    }

    public void setSalvo(Set<Salvo> salvo) {
        this.salvos = salvo;
    }


    public void addSalvo(Salvo salvo) {
        salvos.add(salvo);
    }

    public int theCurrentTurn(){
        int lastTurn=0;

        for (Salvo salvo: salvos){
            if(lastTurn < salvo.getTurn()){
                lastTurn = salvo.getTurn();
            }
        }
        return lastTurn;
    }
}