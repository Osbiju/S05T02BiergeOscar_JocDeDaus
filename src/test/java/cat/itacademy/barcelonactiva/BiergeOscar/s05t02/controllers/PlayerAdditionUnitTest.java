package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.PlayerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class PlayerAdditionUnitTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

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

    @DisplayName("PlayerAdditionUnitTest - createPlayer inserts new player")
    @Test
    void createPlayer() throws Exception{
        PlayerDTO playerDTO = PlayerDTO.builder().playerName("Player 4").build();

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.playerName", is("Player 4")))
                .andExpect(jsonPath("$.winRate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("PlayerAdditionUnitTest - createPlayer returns Exception")
    @Test
    void createPlayer_ExceptionThrowing() throws Exception{
        PlayerDTO playerDTO = PlayerDTO.builder().playerName("Player 1").build();
        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("PlayerAdditionUnitTest - updatePlayer modifies existing player")
    @Test
    void updatePlayer() throws Exception{
        PlayerDTO playerDTO = PlayerDTO.builder().id(1).playerName("Player 1 Updated").build();
        mockMvc.perform(put("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.playerName", is("Player 1 Updated")))
                .andExpect(jsonPath("$.winRate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("PlayerAdditionUnitTest - updatePlayer returns Exception")
    @Test
    void updatePlayer_ExceptionThrowing() throws Exception {
        PlayerDTO playerDTO = PlayerDTO.builder().id(4).playerName("A").build();
        mockMvc.perform(put("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("PlayerAdditionUnitTest - findAll returns list of players")
    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @DisplayName("PlayerAdditionUnitTest - avgWinRate returns average as double")
    @Test
    void avgWinRate() throws Exception {
        mockMvc.perform(get("/players/ranking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(0.5));
    }

    @DisplayName("PlayerAdditionUnitTest - loser returns player with least score")
    @Test
    void loser() throws Exception {

        mockMvc.perform(get("/players/ranking/loser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.playerName", is("Player 3")))
                .andExpect(jsonPath("$.winRate").value(0d))
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("PlayerAdditionUnitTest - winner returns player with highest score")
    @Test
    void winner() throws Exception {

        mockMvc.perform(get("/players/ranking/winner")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.playerName", is("Player 2")))
                .andExpect(jsonPath("$.winRate").value(1d))
                .andExpect(jsonPath("$").isNotEmpty());
    }
}
