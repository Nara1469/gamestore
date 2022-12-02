package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GameRepositoryTest {

    @Autowired
    GameRepository gameRepository;

    @Before
    public void setUp() throws Exception {
        gameRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteGame() {

        //Arrange
        Game newGame = new Game();
        newGame.setQuantity(1);
        newGame.setPrice(10.05);
        newGame.setDescription("Fun Game for all");
        newGame.setEsrbRating("R18");
        newGame.setStudio("AE");
        newGame.setTitle("SimCopter");

        //Act
        newGame = gameRepository.save(newGame);
        Optional<Game> foundGame = gameRepository.findById(newGame.getGameId());

        //Assert
        assertTrue(foundGame.isPresent());

        //Arrange
        newGame.setQuantity(5);
        newGame.setDescription("Suspense in the air");

        //Act
        gameRepository.save(newGame);
        foundGame = gameRepository.findById(newGame.getGameId());

        //Assert
        assertTrue(foundGame.isPresent());

        //Act
        gameRepository.deleteById(newGame.getGameId());
        foundGame = gameRepository.findById(newGame.getGameId());

        //Assert
        assertFalse(foundGame.isPresent());
    }

    @Test
    public void shouldFindAllGame(){
        //Arrange
        Game newGame1 = new Game();
        newGame1.setQuantity(1);
        newGame1.setPrice(10.05);
        newGame1.setDescription("Fun Game for all");
        newGame1.setEsrbRating("R18");
        newGame1.setStudio("AE");
        newGame1.setTitle("SimCopter");

        Game newGame2 = new Game();
        newGame2.setQuantity(1);
        newGame2.setPrice(20.05);
        newGame2.setDescription("Strategy 4 Everyone");
        newGame2.setEsrbRating("E");
        newGame2.setStudio("YoYo");
        newGame2.setTitle("FortLine");

        //Act
        newGame1 = gameRepository.save(newGame1);
        newGame2 = gameRepository.save(newGame2);
        List<Game> allGame = new ArrayList();
        allGame.add(newGame1);
        allGame.add(newGame2);

        //Act
        List<Game> foundAllGame = gameRepository.findAll();

        //Assert
        assertEquals(allGame.size(),foundAllGame.size());
    }

    @Test
    public void shouldFindGameByEsrb(){
        //Arrange
        Game newGame1 = new Game();
        newGame1.setQuantity(1);
        newGame1.setPrice(10.05);
        newGame1.setDescription("Fun Game for all");
        newGame1.setEsrbRating("E");
        newGame1.setStudio("AE");
        newGame1.setTitle("SimCopter");

        Game newGame2 = new Game();
        newGame2.setQuantity(7);
        newGame2.setPrice(20.05);
        newGame2.setDescription("Strategy 4 Everyone");
        newGame2.setEsrbRating("E");
        newGame2.setStudio("YoYo");
        newGame2.setTitle("FortLine");

        Game newGame3 = new Game();
        newGame3.setQuantity(3);
        newGame3.setPrice(19.05);
        newGame3.setDescription("NeverLand");
        newGame3.setEsrbRating("PG");
        newGame3.setStudio("GameMaker");
        newGame3.setTitle("Hook");
        //Act
        newGame1 = gameRepository.save(newGame1);
        newGame2 = gameRepository.save(newGame2);
        newGame3 = gameRepository.save(newGame3);

        //Act
        List<Game> foundNoGame = gameRepository.findAllGamesByEsrbRating("InvalidValue");

        List<Game> foundOneGame = gameRepository.findAllGamesByEsrbRating("PG");

        List<Game> foundTwoGame = gameRepository.findAllGamesByEsrbRating("E");

        //Assert
        assertEquals(foundNoGame.size(),0);
        assertEquals(foundOneGame.size(),1);
        assertEquals(foundTwoGame.size(),2);
    }

    @Test
    public void shouldFindGameByStudio(){
        //Arrange
        Game newGame1 = new Game();
        newGame1.setQuantity(1);
        newGame1.setPrice(10.05);
        newGame1.setDescription("Fun Game for all");
        newGame1.setEsrbRating("E");
        newGame1.setStudio("AE");
        newGame1.setTitle("SimCopter");

        Game newGame2 = new Game();
        newGame2.setQuantity(7);
        newGame2.setPrice(20.05);
        newGame2.setDescription("Strategy 4 Everyone");
        newGame2.setEsrbRating("E");
        newGame2.setStudio("YoYo");
        newGame2.setTitle("FortLine");

        Game newGame3 = new Game();
        newGame3.setQuantity(3);
        newGame3.setPrice(19.05);
        newGame3.setDescription("NeverLand");
        newGame3.setEsrbRating("PG");
        newGame3.setStudio("AE");
        newGame3.setTitle("Hook");

        //Act
        newGame1 = gameRepository.save(newGame1);
        newGame2 = gameRepository.save(newGame2);
        newGame3 = gameRepository.save(newGame3);

        //Act
        List<Game> foundNoGame = gameRepository.findAllGamesByStudio("InvalidValue");

        List<Game> foundOneGame = gameRepository.findAllGamesByStudio("YoYo");

        List<Game> foundTwoGame = gameRepository.findAllGamesByStudio("AE");

        //Assert
        assertEquals(foundNoGame.size(),0);
        assertEquals(foundOneGame.size(),1);
        assertEquals(foundTwoGame.size(),2);
    }

    @Test
    public void shouldFindGameByTitle(){
        //Arrange
        Game newGame1 = new Game();
        newGame1.setQuantity(1);
        newGame1.setPrice(10.05);
        newGame1.setDescription("Fun Game for all");
        newGame1.setEsrbRating("E");
        newGame1.setStudio("AE");
        newGame1.setTitle("SimCopter");

        Game newGame2 = new Game();
        newGame2.setQuantity(7);
        newGame2.setPrice(20.05);
        newGame2.setDescription("Strategy 4 Everyone");
        newGame2.setEsrbRating("E");
        newGame2.setStudio("YoYo");
        newGame2.setTitle("FortLine");

        Game newGame3 = new Game();
        newGame3.setQuantity(3);
        newGame3.setPrice(19.05);
        newGame3.setDescription("NeverLand");
        newGame3.setEsrbRating("PG");
        newGame3.setStudio("AE");
        newGame3.setTitle("Hook");

        //Act
        newGame1 = gameRepository.save(newGame1);
        newGame2 = gameRepository.save(newGame2);
        newGame3 = gameRepository.save(newGame3);

        //Act
        List<Game> foundNoGame = gameRepository.findAllGamesByTitle("InvalidValue");

        List<Game> foundOneGame = gameRepository.findAllGamesByTitle("Hook");

        //Assert
        assertEquals(foundNoGame.size(),0);
        assertEquals(foundOneGame.size(),1);
    }
}