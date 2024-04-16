package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.GameDTO;

import java.util.List;

public interface GameService {
    GameDTO playerPlayGame(int id);
    void deleteGames(int id);
    List<GameDTO> listGamesByPlayer(int id);
}
