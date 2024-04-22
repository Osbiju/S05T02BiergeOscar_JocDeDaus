package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.repository;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.PlayerRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerRepositoryUnitTest {

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeAll
    static void reset(@Autowired JdbcTemplate jdbcTemplate){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "players");

        jdbcTemplate.execute("INSERT INTO players (id, player_name, registration_date) VALUES (1, 'Player 1', '" + LocalDateTime.now() + "');");

    }
    @DisplayName("PlayerRepositoryUnitTest - existsByPlayerName returns boolean")
    @Test
    void existsByPlayerName() {
        boolean existsByPlayerName = playerRepository.existsByPlayerName("Player 1");
        assertTrue(existsByPlayerName);

        existsByPlayerName = playerRepository.existsByPlayerName("Player 7");
        assertFalse(existsByPlayerName);
    }
}
