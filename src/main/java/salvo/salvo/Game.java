package salvo.salvo;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ricard Esteve on 17/07/2017.
 */

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private Date creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    public Game() { creationDate = new Date(); }

    public Game(Date creationDate) {
        this.creationDate = creationDate;

    }

    public long getId () {
        return this.id;
    }

    public Date getCreationDate() {
        return creationDate;
    }


    public void setCreationDate( Date newCreationDate) {
        this.creationDate = newCreationDate;
    }


    public void addSeconds (int seconds){

        this.creationDate =  creationDate.from(creationDate.toInstant().plusSeconds(seconds));
    }



}

