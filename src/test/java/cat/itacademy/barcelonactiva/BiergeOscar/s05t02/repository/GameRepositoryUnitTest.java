package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.repository;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.Game;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.GameRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameRepositoryUnitTest {
    @Autowired
    GameRepository gameRepository;

    @BeforeAll
    static void reset(@Autowired JdbcTemplate jdbcTemplate){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "players");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "games");

        jdbcTemplate.execute("INSERT INTO players (id, player_name, registration_date) VALUES (1, 'Player 1', '" + LocalDateTime.now() + "');");

        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (1, 3, 4, 1);");
        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (2, 4, 5, 1);");
    }

    @DisplayName("GameRepositoryUnitTest - findByPlayerId returns list")
    @Test
    void findByPlayerId(){
        List<Game> games = gameRepository.findByPlayerId(1);
        assertEquals(2, games.size());
    }

    @DisplayName("GameRepositoryUnitTest - deletById deletes all games from player")
    @Test
    void deleteById(){
        gameRepository.deleteByPlayerId(1);
        List<Game> games = gameRepository.findByPlayerId(1);
        assertEquals(0,games.size());
    }
}
