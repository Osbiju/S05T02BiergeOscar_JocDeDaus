package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
}
