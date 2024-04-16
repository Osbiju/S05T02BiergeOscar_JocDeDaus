package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private int gameID;
    private int playerID;
    private int diceOne;
    private int diceTwo;
    private Boolean won;
}
