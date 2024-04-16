package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.PlayerDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.exceptions.PlayerAlreadyExistsException;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/players")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerDTO playerDTO){
        try {
            if (playerDTO.getPlayerName() == null || playerDTO.getPlayerName().isEmpty()) {
                return ResponseEntity.badRequest().body("Player name cannot be empty");
            }
            if (playerDTO.getRegistrationDate() == null) {
                playerDTO.setRegistrationDate(LocalDateTime.now());
            }
            if (playerDTO.getWinRate() == 0.0) {
                playerDTO.setWinRate(0.0);
            }
            PlayerDTO createdPlayer = playerService.save(playerDTO);
            System.out.println("Player created successfully: " + createdPlayer.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
        } catch(PlayerAlreadyExistsException e) {
            System.out.println("Player creation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already exists a player with this name.");
        } catch(Exception e) {
            System.out.println("Player creation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the player: " + e.getMessage());
        }
    }

    @PutMapping("/players")
    public ResponseEntity<PlayerDTO> updatePlayer(@RequestBody PlayerDTO playerDTO){
        return ResponseEntity.ok(playerService.update(playerDTO));
    }

    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(){
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping("/players/ranking")
    public ResponseEntity<Double> getAverageWinRate() {
        return ResponseEntity.ok(playerService.avgWinRate());
    }

    @GetMapping("/players/ranking/loser")
    public ResponseEntity<PlayerDTO> getLoser() {
        return ResponseEntity.ok(playerService.loser());
    }

    @GetMapping("/players/ranking/winner")
    public ResponseEntity<PlayerDTO> getWinner() {
        return ResponseEntity.ok(playerService.winner());
    }
}
