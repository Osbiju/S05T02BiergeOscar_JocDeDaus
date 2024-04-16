package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gameID;

    @Column(name = "dice_one")
    private int diceOne;

    @Column(name = "dice_two")
    private int diceTwo;

    @Column(name="result")
    private Boolean won;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "playerID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Player player;

    public Game(Player player){
        this.player = player;
    }
    public Boolean won(){
        return diceOne + diceTwo == 7;
    }
}
