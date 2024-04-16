package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.GameDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.GameService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class GameController {

    @Autowired
    GameService gameService;

    @PostMapping("/players/{id}/games")
    public ResponseEntity<GameDTO> playGame(@PathVariable("id") Integer id, HttpServletRequest request) {
        request.getHeader("username");
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.playerPlayGame(id));
    }
    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<GameDTO> deleteGames(@PathVariable("id") Integer id) {
        gameService.deleteGames(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/players/{id}/games")
    public ResponseEntity<List<GameDTO>> getGames(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(gameService.listGamesByPlayer(id));
    }
}
