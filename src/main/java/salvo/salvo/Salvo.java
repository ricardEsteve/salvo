package salvo.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricard Esteve on 28/07/2017.
 */

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int turn;

    @JoinColumn(name = "gamePlayer_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "cells")
    private List<String> cells = new ArrayList<>();

    public Salvo() {}

    public Salvo(GamePlayer gamePlayer, int turn, List<String> cells){
        this.gamePlayer= gamePlayer;
        this.turn= turn;
        gamePlayer.addSalvo(this);
        this.cells= cells;

    }
    public long getId(){
        return id;
    }
    public GamePlayer getGamePlayer (){
        return gamePlayer;
    }
    public void setGamePlayer (GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    public List<String> getCells (){
        return cells;
    }

    public void setCells(List<String> cellsList) {
        this.cells = cellsList;
    }

    public int getTurn(){
        return turn;
    }

    public void setTurn (int turn){
        this.turn = turn;
    }
}


