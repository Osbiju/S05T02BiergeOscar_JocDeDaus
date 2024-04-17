package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.repository;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository.PlayerRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDateTime;

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
}
