package salvo.salvo;

/**
 * Created by Ricard Esteve on 11/07/2017.
 */



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Entity
public class Player {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String userName;
    private String email;

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
}


