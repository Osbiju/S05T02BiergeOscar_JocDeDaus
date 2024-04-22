package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class GameAdditionUnitTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void reset(@Autowired JdbcTemplate jdbcTemplate){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "players");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "games");

        jdbcTemplate.execute("INSERT INTO players (id, player_name, registration_date) VALUES (1, 'Player 1', '" + LocalDateTime.now() + "');");
        jdbcTemplate.execute("INSERT INTO players (id, player_name, registration_date) VALUES (2, 'Player 2', '" + LocalDateTime.now() + "');");
        jdbcTemplate.execute("INSERT INTO players (id, player_name, registration_date) VALUES (3, 'Player 3', '" + LocalDateTime.now() + "');");

        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (1, 3, 4, 1);");
        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (2, 1, 3, 1);");
        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (3, 5, 2, 2);");
        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (4, 6, 1, 2);");
        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (5, 4, 5, 3);");
        jdbcTemplate.execute("INSERT INTO games (gameid, dice_one, dice_two, playerid) VALUES (6, 6, 2, 3);");
    }


    @DisplayName("GameAdditionUnitTest - playGame inserts new game")
    @Test
    void playGame() throws Exception{
        mockMvc.perform(post("/players/1/games"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gameID", is(552)))//cada vez q runea suma 50 puestos
                .andExpect(jsonPath("$.playerID", is(1)))
                .andExpect(jsonPath("$.diceOne").isNotEmpty())
                .andExpect(jsonPath("$.diceTwo").isNotEmpty())
                .andExpect(jsonPath("$.won").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("GameAdditionUnitTest - playGame throws EntityNotFoundException if player does not exists")
    @Test
    void playGame_ExceptionThrowing() throws Exception{
        mockMvc.perform(post("/players/7/games").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("GameAdditionUnitTest - deleteGames removes games")
    @Test
    void deleteGames() throws Exception {
        mockMvc.perform(delete("/players/1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("GameAdditionUnitTest - deleteGames throws EntityNotFoundException if player does not exist")
    @Test
    void deleteGames_ExceptionThrowing() throws Exception{
        mockMvc.perform(delete("/players/7/games")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @DisplayName("GameAdditionUnitTest - getGames returns list of games")
    @Test
    void getGames() throws Exception{
        mockMvc.perform(get("/players/1/games")
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
    @DisplayName("GameAdditionUnitTest - getGames throws EntityNotFoundException if player does not exist")
    @Test
    void getGames_ExceptionThrowing() throws Exception{
        mockMvc.perform(get("/players/7/games")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
