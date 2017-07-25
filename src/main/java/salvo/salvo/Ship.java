package salvo.salvo;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricard Esteve on 20/07/2017.
 */
@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private ShipType type;


    @JoinColumn(name = "gamePlayer_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private GamePlayer gamePlayer;


    @ElementCollection
    @Column(name = "cells")
    private List<String> cells = new ArrayList<>();

    public Ship() {}

    public Ship(ShipType shipType, GamePlayer gamePlayer,  List<String> cells) {
        this.type = shipType;
        this.gamePlayer = gamePlayer;
        this.cells = cells;
    }

    public long getId() {
        return id;
    }

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType shipType ){
        this.type = shipType;
    }

    public GamePlayer getGamePlayer(){
        return gamePlayer;
    }

    public void setGamePlayer (GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    public List<String> getCells (){
        return cells;
    }

    public void setCells(List<String> cellList) {
        this.cells = cellList;
    }


    }



