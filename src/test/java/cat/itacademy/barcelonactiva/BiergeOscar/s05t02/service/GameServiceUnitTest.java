package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.service;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.GameDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.Game;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.Player;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.GameRepository;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.implementations.GameServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceUnitTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private GameServiceImpl gameService;

    private Player player;

    @BeforeEach
    void setUp(){
        player = Player.builder()
                .id(1)
                .playerName("Player 1")
                .registrationDate(LocalDateTime.of(1111,11,11,11,11)).build();
    }

    @AfterEach
    void reset(){
        player = null;
    }

    @DisplayName("GameServiceUnitTest - playerPlayGame inserts new game")
    @Test
    void playerPlayGame() {
        Game game;
        GameDTO gameDTO;

        when(gameRepository.save(any(Game.class))).thenAnswer(i-> i.getArguments()[0]);
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        gameDTO = gameService.playerPlayGame(1);

        ArgumentCaptor<Game> gameEntityArgumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository, times(1)).save(gameEntityArgumentCaptor.capture());

        game = gameEntityArgumentCaptor.getValue();
        assertEquals(game.getDiceOne(), gameDTO.getDiceOne());
        assertEquals(game.getDiceTwo(), gameDTO.getDiceTwo());
    }

    @DisplayName("GameServiceUnitTest - playerPlayGames throws EntityNotFoundException if player does not exist ")
    @Test
    void playerPlayGame_ExceptionThrowing(){
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.playerPlayGame(1));
    }

    @DisplayName("GameServiceUnitTest - findByPlayerId return list of GameDto")
    @Test
    void findByPlayerId(){
        List<GameDTO> games;
        Game game1 = Game.builder()
                .gameID(1)
                .diceOne(3)
                .diceTwo(4)
                .player(player)
                .build();
        Game game2 = Game.builder()
                .gameID(2)
                .diceOne(5)
                .diceTwo(4)
                .player(player)
                .build();

        when(playerRepository.findById(1)).thenReturn(Optional.of(player));
        when((gameRepository.findByPlayerId(1))).thenReturn(List.of(game1, game2));

        games = gameService.listGamesByPlayer(1);
        assertEquals(2, games.size());
        verify(playerRepository).findById(1);
        verify(gameRepository).findByPlayerId(1);
    }

    @DisplayName("GameServiceUnitTest - findByPlayerId throws exception if player cannot be found ")
    @Test
    void findByPlayerId_ExceptionThrowing(){
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.listGamesByPlayer(1));
    }

    @DisplayName("GameServiceUnitTest - deleteGames deletes games belonging to that player")
    @Test
    void deleteGames(){
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));
        gameService.deleteGames(1);
        verify(gameRepository).deleteByPlayerId(1);
    }

    @DisplayName("GameServiceUnitTest - deleteGame throws exception if player cannot be found")
    @Test
    void deleteGames_ExceptionThrowing(){
        when(playerRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> gameService.deleteGames(1));
    }

}
