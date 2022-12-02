package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.Game;
import com.trilogyed.gamestore.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameRepository gameRepository;

    //used to move between Objects and JSON
    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldCreateGame() throws Exception{
        //Arrange
        Game inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        String inputJson = mapper.writeValueAsString(inGame);

        Game savedGame = new Game();
        savedGame.setTitle("Halo");
        savedGame.setEsrbRating("E10+");
        savedGame.setDescription("Puzzles and Math");
        savedGame.setPrice(23.99);
        savedGame.setStudio("Xbox Game Studios");
        savedGame.setQuantity(5);
        savedGame.setGameId(51);

        String outputJson = mapper.writeValueAsString(savedGame);

        doReturn(savedGame).when(gameRepository).save(inGame);

        //Act & Assert
        this.mockMvc.perform(post("/game")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnGameInfo() throws Exception{

        //Object to JSON in String
        String outputJson = null;

        //Arrange
        Game game = new Game();
        game.setTitle("Halo");
        game.setEsrbRating("E10+");
        game.setDescription("Puzzles and Math");
        game.setPrice(23.99);
        game.setStudio("Xbox Game Studios");
        game.setQuantity(5);
        game.setGameId(8);

        outputJson = mapper.writeValueAsString(game);

        doReturn(Optional.of(game)).when(gameRepository).findById(8);

        //Act & Assert
        this.mockMvc.perform(get("/game/8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldStatus404ForNonExistentGameId() throws Exception{
        doReturn(Optional.empty()).when(gameRepository).findById(88888);

        mockMvc.perform(
                        get("/game/88888")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateGame() throws Exception{
        //Object to JSON in String
        String inputJson = null;

        //Arrange
        Game inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(62);

        inputJson = mapper.writeValueAsString(inGame);

        doReturn(null).when(gameRepository).save(inGame);

        //Act & Assert
        this.mockMvc.perform(put("/game")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteGame() throws Exception{
        //Object to JSON in String
        String inputJson=null;

        //Arrange
        Game inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(62);

        inputJson = mapper.writeValueAsString(inGame);

        doReturn(Optional.ofNullable(inGame)).when(gameRepository).findById(62);

        //Act & Assert
        this.mockMvc.perform(delete("/game/62"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldGetGamesByTitle() throws Exception{
        //Object to JSON in String
        String outputJson = null;

        //Arrange
        Game savedGame1 = new Game();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(23.99);
        savedGame1.setStudio("Xbox Game Studios");
        savedGame1.setQuantity(5);
        savedGame1.setGameId(56);

        Game savedGame2 = new Game();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(23.99);
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setGameId(51);

        Game savedGame3 = new Game();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E10+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(23.99);
        savedGame3.setStudio("Xbox Game Studios");
        savedGame3.setQuantity(5);
        savedGame3.setGameId(77);

        List<Game> foundList = new ArrayList();
        foundList.add(savedGame1);

        outputJson = mapper.writeValueAsString(foundList);

        doReturn(foundList).when(gameRepository).findAllGamesByTitle("Halo");

        //Act & Assert
        this.mockMvc.perform(get("/game/title/Halo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetGamesByEsrbRating() throws Exception{
        //Object to JSON in String
        String outputJson = null;

        //Arrange
        Game savedGame1 = new Game();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(23.99);
        savedGame1.setStudio("Xbox Game Studios");
        savedGame1.setQuantity(5);
        savedGame1.setGameId(56);

        Game savedGame2 = new Game();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(23.99);
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setGameId(51);

        Game savedGame3 = new Game();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E18+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(23.99);
        savedGame3.setStudio("Xbox Game Studios");
        savedGame3.setQuantity(5);
        savedGame3.setGameId(77);

        List<Game> foundList = new ArrayList();
        foundList.add(savedGame1);
        foundList.add(savedGame2);

        outputJson = mapper.writeValueAsString(foundList);

        doReturn(foundList).when(gameRepository).findAllGamesByEsrbRating("10+");

        //Act & Assert
        this.mockMvc.perform(get("/game/esrb/10+"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetGamesByStudio() throws Exception{
        //Object to JSON in String
        String outputJson = null;

        //Arrange
        Game savedGame1 = new Game();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(23.99);
        savedGame1.setStudio("A&E");
        savedGame1.setQuantity(5);
        savedGame1.setGameId(56);

        Game savedGame2 = new Game();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(23.99);
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setGameId(51);

        Game savedGame3 = new Game();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E18+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(23.99);
        savedGame3.setStudio("A&E");
        savedGame3.setQuantity(5);
        savedGame3.setGameId(77);

        List<Game> foundList = new ArrayList();
        foundList.add(savedGame1);
        foundList.add(savedGame3);

        outputJson = mapper.writeValueAsString(foundList);

        doReturn(foundList).when(gameRepository).findAllGamesByStudio("A&E");

        //Act & Assert
        this.mockMvc.perform(get("/game/studio/A&E"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldGetAllGames() throws Exception{
        //Object to JSON in String
        String outputJson = null;

        //Arrange
        Game savedGame1 = new Game();
        savedGame1.setTitle("Halo");
        savedGame1.setEsrbRating("E10+");
        savedGame1.setDescription("Puzzles and Math");
        savedGame1.setPrice(23.99);
        savedGame1.setStudio("A&E");
        savedGame1.setQuantity(5);
        savedGame1.setGameId(56);

        Game savedGame2 = new Game();
        savedGame2.setTitle("Halo I");
        savedGame2.setEsrbRating("E10+");
        savedGame2.setDescription("Puzzles and Math");
        savedGame2.setPrice(23.99);
        savedGame2.setStudio("Xbox Game Studios");
        savedGame2.setQuantity(5);
        savedGame2.setGameId(51);

        Game savedGame3 = new Game();
        savedGame3.setTitle("Halo IV");
        savedGame3.setEsrbRating("E18+");
        savedGame3.setDescription("Puzzles and Math");
        savedGame3.setPrice(23.99);
        savedGame3.setStudio("A&E");
        savedGame3.setQuantity(5);
        savedGame3.setGameId(77);

        List<Game> foundList = new ArrayList();
        foundList.add(savedGame1);
        foundList.add(savedGame3);

        outputJson = mapper.writeValueAsString(foundList);

        doReturn(foundList).when(gameRepository).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/game"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldFailCreateGameWithInvalidData() throws Exception {

        //Arrange
        //title...
        Game inGame = new Game();
        inGame.setTitle("");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        when(this.gameRepository.save(inGame)).thenReturn(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Esrb...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        doReturn(Optional.ofNullable(inGame)).when(gameRepository).save(inGame);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.


        //Desc...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Studio...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Price...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(-1.00);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(60000.00);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(null);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //quantity
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(0);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(50001);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldFailUpdateGameWithInvalidData() throws Exception {
        //Arrange
        Game inGame = new Game();
        inGame.setTitle("");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(77);

        when(this.gameRepository.findById(77)).thenReturn(Optional.ofNullable(inGame));

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()) //Expected response status code.
        ;

        //Esrb...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(77);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.


        //Desc...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Studio...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("");
        inGame.setQuantity(5);
        inGame.setGameId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //Price...
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(-1.00);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(60000.00);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(null);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(5);
        inGame.setGameId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        //quantity
        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(0);
        inGame.setGameId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.

        inGame = new Game();
        inGame.setTitle("Halo");
        inGame.setEsrbRating("E10+");
        inGame.setDescription("Puzzles and Math");
        inGame.setPrice(23.99);
        inGame.setStudio("Xbox Game Studios");
        inGame.setQuantity(50001);
        inGame.setGameId(77);

        //ResultActions x = mockMvc.perform(
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/game")
                                .content(mapper.writeValueAsString(inGame)) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isUnprocessableEntity()); //Expected response status code.
    }

    @Test
    public void shouldFailFindGamesWithInvalidData() throws Exception {
        List<Game> emptyList = new ArrayList();
        String badValue = "bad value";

        when(this.gameRepository.findAllGamesByStudio(badValue)).thenReturn(emptyList);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/game/studio/{badValue}", badValue) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.

        when(this.gameRepository.findAllGamesByEsrbRating(badValue)).thenReturn(emptyList);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/game/esrb/{badValue}", badValue) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.

        when(this.gameRepository.findAllGamesByTitle(badValue)).thenReturn(emptyList);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/game/title/{badValue}", badValue) //converts object to JSON and places into RequestBody
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //for debugging purposes. Prints the request, handler,... and response objects to the game below.
                .andExpect(status().isNotFound()); //Expected response status code.
    }
}