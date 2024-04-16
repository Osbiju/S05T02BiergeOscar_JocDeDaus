package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private int id;
    private String playerName;
    private LocalDateTime registrationDate;
    private double winRate;
}
