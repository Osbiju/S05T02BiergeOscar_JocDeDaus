package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.PlayerDTO;

import java.util.List;

public interface PlayerService {
    PlayerDTO save (PlayerDTO playerDTO);
    PlayerDTO update (PlayerDTO playerDTO);
    PlayerDTO findById (int id);
    List<PlayerDTO> findAll ();
    double avgWinRate();
    PlayerDTO loser();
    PlayerDTO winner();
}
