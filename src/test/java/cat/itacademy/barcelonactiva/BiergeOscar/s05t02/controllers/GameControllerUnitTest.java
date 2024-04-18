package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.controllers;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO.GameDTO;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.security.JwtFilter;
import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.service.interfaces.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GameController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GameControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private GameDTO gameDTO;

    @BeforeEach
    void setUp(){
        gameDTO = GameDTO.builder()
                .gameID(1)
                .playerID(1)
                .diceOne(3)
                .diceTwo(4)
                .won(true)
                .build();
    }

    @AfterEach
    void reset(){
        gameDTO = null;
    }

    @DisplayName("GameControllerUnitTest - createPlayer creates a new player")
    @Test
    void createPlayer() throws Exception{
        when(gameService.playerPlayGame(anyInt())).thenReturn(gameDTO);
        mockMvc.perform(post("/players/1/games"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gameID", is(1)))
                .andExpect(jsonPath("$.playerID", is(1)))
                .andExpect(jsonPath("$.diceOne", is(3)))
                .andExpect(jsonPath("$.diceTwo", is(4)))
                .andExpect(jsonPath("$.won", is(true)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("GameControllerUnitTest - deleteGames removes games")
    @Test
    void deleteGames() throws  Exception{
        mockMvc.perform(delete("/players/1/games"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("GameControllerUnitTest - getGames return a list of games")
    @Test
    void getGames() throws Exception{
        GameDTO gameDTO1 = GameDTO.builder()
                .gameID(2)
                .playerID(1)
                .diceOne(2)
                .diceTwo(4)
                .won(false)
                .build();
        when(gameService.listGamesByPlayer(anyInt())).thenReturn(List.of(gameDTO, gameDTO1));
        mockMvc.perform(get("/players/1/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$").isArray());
    }
}
