package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.service;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.PlayerDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.Game;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.Player;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.exceptions.InvalidUsernameException;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.GameRepository;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.implementations.PlayerServiceImpl;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.PlayerService;
import jakarta.persistence.EntityExistsException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceUnitTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private PlayerServiceImpl playerService;

    private PlayerDTO playerDTO;
    private Player player;
    private Player player2;

    @BeforeEach
    void setUp() {
        playerDTO = PlayerDTO.builder()
                .id(1)
                .playerName("Player 1")
                .registrationDate(LocalDateTime.of(1111, 11, 11, 11, 11)).build();
        player = Player.builder()
                .id(1)
                .playerName("Player 1")
                .registrationDate(LocalDateTime.of(1111, 11, 11, 11, 11)).build();
        player2 = Player.builder()
                .id(2)
                .playerName("Player 2")
                .registrationDate(LocalDateTime.of(1111, 11, 11, 11, 11)).build();
    }
    @AfterEach
    void reset() {
        playerDTO = null;
        player = null;
        player2 = null;
    }
    @DisplayName("PlayerServiceUnitTest - Save inserts new player")
    @Test
    void save() {
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        playerService.save(playerDTO);

        ArgumentCaptor<Player> playerEntityArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository, times(1)).save(playerEntityArgumentCaptor.capture());

        player = playerEntityArgumentCaptor.getValue();
        assertEquals(player.getPlayerName(), "Player 1");
    }

    @DisplayName("PlayerServiceUnitTest - Save assigns default name")
    @Test
    void saveDefaultName() {
        ArgumentCaptor<Player> playerEntityArgumentCaptor = ArgumentCaptor.forClass(Player.class);

        playerDTO.setPlayerName(null);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        playerService.save(playerDTO);
        verify(playerRepository, times(1)).save(playerEntityArgumentCaptor.capture());
        assertEquals(playerEntityArgumentCaptor.getValue().getPlayerName(), "ANONIM");

    }
    @DisplayName("PlayerServiceUnitTest - Save throws exception if player name already exists")
    @Test
    void save_ExceptionThrowing() {

        when(playerRepository.existsByPlayerName("Player 1")).thenReturn(true);

        assertThrows(InvalidUsernameException.class, () -> playerService.save(playerDTO));
    }
    @DisplayName("PlayerServiceUnitTest - Save accepts multiple players with default name")
    @Test
    void saveMultipleDefaultNames() {
        PlayerDTO playerDTO2 = PlayerDTO.builder().id(2).playerName("ANONIM").build();
        ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
        List<Player> players;

        playerDTO.setPlayerName(null);
        player.setPlayerName(null);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        playerService.save(playerDTO);
        playerService.save(playerDTO2);

        verify(playerRepository, times(2)).save(playerArgumentCaptor.capture());

        players = playerArgumentCaptor.getAllValues();

        assertEquals(players.get(0).getPlayerName(), players.get(1).getPlayerName());
        assertEquals(players.get(1).getPlayerName(), "ANONIM");
        assertNotEquals(players.get(0).getId(), players.get(1).getId());
    }
    @DisplayName("PlayerServiceUnitTest - Update modifies existing player")
    @Test
    void update() {
        ArgumentCaptor<Player> playerEntityArgumentCaptor = ArgumentCaptor.forClass(Player.class);

        when(playerRepository.save(any(Player.class))).thenReturn(player);
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        playerDTO.setPlayerName("Modified name");
        playerService.update(playerDTO);
        verify(playerRepository, times(1)).save(playerEntityArgumentCaptor.capture());

        player = playerEntityArgumentCaptor.getValue();
        assertEquals(player.getPlayerName(), "Modified name");
    }
    @DisplayName("PlayerServiceUnitTest - Update throws exception if player cannot be found")
    @Test
    void update_ExceptionThrowing() {
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        playerDTO.setPlayerName("Modified name");
        assertThrows(EntityNotFoundException.class, () -> playerService.update(playerDTO));
    }
    @DisplayName("PlayerServiceUnitTest - findById returns playerDTO")
    @Test
    void findById() {
        PlayerDTO playerDTO2;

        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        playerDTO2 = playerService.findById(1);
        assertEquals(playerDTO.getId(), playerDTO2.getId());
        assertEquals(playerDTO.getPlayerName(), playerDTO2.getPlayerName());
        verify(playerRepository).findById(1);
    }

    @DisplayName("PlayerServiceUnitTest - findById throws exception if player cannot be found")
    @Test
    void findById_ExceptionThrowing() {
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> playerService.update(playerDTO));
    }

    @DisplayName("PlayerServiceUnitTest - findAll returns list of PlayerDTO")
    @Test
    void findAll() {
        List<PlayerDTO> players;

        when(playerRepository.findAll()).thenReturn(List.of(player, player2));

        players = playerService.findAll();

        assertEquals(2, players.size());
        verify(playerRepository).findAll();
    }

    @DisplayName("PlayerServiceUnitTest - avgWinRate returns double")
    @Test
    void avgWinRate() {
        Game game1 = Game.builder().diceOne(3).diceTwo(4).build();
        Game game2 = Game.builder().diceOne(2).diceTwo(4).build();

        when(playerRepository.findAll()).thenReturn(List.of(player, player2));
        when(gameRepository.findByPlayerId(anyInt())).thenReturn(List.of(game1, game2));

        double avg = playerService.avgWinRate();

        assertEquals(0.5d, avg);
    }

    @DisplayName("PlayerServiceUnitTest - avgWinRate returns zero if there are no players")
    @Test
    void avgWinRate_NoPlayers() {
        when(playerRepository.findAll()).thenReturn(new ArrayList<>());

        double avg = playerService.avgWinRate();

        assertEquals(0d, avg);
        verifyNoInteractions(gameRepository);
    }

    @DisplayName("PlayerServiceUnitTest - avgWinRate returns zero if there are no games")
    @Test
    void avgWinRate_NoGames() {
        when(playerRepository.findAll()).thenReturn(List.of(player, player2));
        when(gameRepository.findByPlayerId(anyInt())).thenReturn(new ArrayList<>());

        double avg = playerService.avgWinRate();

        assertEquals(0d, avg);
    }

    @DisplayName("PlayerServiceUnitTest - loser returns PlayerDTO")
    @Test
    void loser() {
        PlayerDTO loser;
        Game game1 = Game.builder().diceOne(3).diceTwo(4).build();
        Game game2 = Game.builder().diceOne(2).diceTwo(4).build();

        when(playerRepository.findAll()).thenReturn(List.of(player, player2));
        when(gameRepository.findByPlayerId(1)).thenReturn(List.of(game1, game2));
        when(gameRepository.findByPlayerId(2)).thenReturn(List.of(game1));

        loser = playerService.loser();

        assertEquals("Player 1", loser.getPlayerName());
    }

    @DisplayName("PlayerServiceUnitTest - winner returns PlayerDTO")
    @Test
    void winner() {
        PlayerDTO winner;

        Game game1 = Game.builder().diceOne(3).diceTwo(4).build();
        Game game2 = Game.builder().diceOne(2).diceTwo(4).build();

        when(playerRepository.findAll()).thenReturn(List.of(player, player2));
        when(gameRepository.findByPlayerId(1)).thenReturn(List.of(game1, game2));
        when(gameRepository.findByPlayerId(2)).thenReturn(List.of(game1));

        winner = playerService.winner();

        assertEquals("Player 2", winner.getPlayerName());
    }
}
