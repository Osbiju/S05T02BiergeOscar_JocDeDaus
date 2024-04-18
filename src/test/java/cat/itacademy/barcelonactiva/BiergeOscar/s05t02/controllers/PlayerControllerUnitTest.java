package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.PlayerDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.security.JwtFilter;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PlayerControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private PlayerDTO playerDTO1;
    private PlayerDTO playerReturnDTO1;

    @BeforeEach
    void setUp() {
        playerDTO1 = PlayerDTO.builder()
                .playerName("Player 1")
                .build();
        playerReturnDTO1 = PlayerDTO.builder()
                .id(1)
                .playerName("Player 1")
                .registrationDate(LocalDateTime.of(1111, 11, 11, 11, 11))
                .winRate(0)
                .build();
    }

    @AfterEach
    void reset() {
        playerDTO1 = null;
        playerReturnDTO1 = null;
    }

    @DisplayName("PlayerControllerUnitTest - createPlayer inserts new player")
    @Test
    void createPlayer() throws Exception {
        when(playerService.save(any(PlayerDTO.class))).thenReturn(playerReturnDTO1);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.winRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("PlayerControllerUnitTest - updatePlayer updates existing player")
    @Test
    void updatePlayer() throws Exception {
        when(playerService.update(any(PlayerDTO.class))).thenReturn(playerReturnDTO1);

        mockMvc.perform(put("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.winRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("PlayerContrololerUnitTest - findAll returns all player with their average scores")
    @Test
    void findAll() throws Exception {
        PlayerDTO playerReturnDTO2 = PlayerDTO.builder()
                .id(2)
                .playerName("Player 2")
                .registrationDate(LocalDateTime.of(1111, 11, 11, 11, 11))
                .winRate(0.5)
                .build();
        when(playerService.findAll()).thenReturn(List.of(playerReturnDTO1, playerReturnDTO2));

        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$").isArray());
    }

    @DisplayName("PlayerControllerUnitTest - getAverageWinRate returns global score")
    @Test
    void getAverageWinRate() throws Exception {
        when(playerService.avgWinRate()).thenReturn(0.25d);
        mockMvc.perform(get("/players/ranking"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(0.25d));
    }

    @DisplayName("PlayerControllerUnitTest - loser return players")
    @Test
    void loser() throws Exception {
        when(playerService.loser()).thenReturn(playerReturnDTO1);

        mockMvc.perform(get("/players/ranking/loser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.winRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("PlayerControllerUnitTest - winner returns player")
    @Test
    void winner() throws Exception {
        when(playerService.winner()).thenReturn(playerReturnDTO1);

        mockMvc.perform(get("/players/ranking/winner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.winRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }


}
