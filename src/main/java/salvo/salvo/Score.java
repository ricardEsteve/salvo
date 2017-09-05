package salvo.salvo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ricard Esteve on 21/08/2017.
 */
@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date finishDate;
    private double points;

    @JoinColumn(name = "game_id")
    @ManyToOne (fetch = FetchType.EAGER)
    private Game game;

    @JoinColumn(name = "player_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Player player;


    public Score() {finishDate = new Date();
    }

    public Score(Game game, Player player, Date date, double points){
        this.game = game;
        this.player = player;
        this.finishDate = date;
        this.points = points;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }
}

