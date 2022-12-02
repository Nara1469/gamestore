package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.Game;
import com.trilogyed.gamestore.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = {"http://localhost:3000"})
public class GameController {
    @Autowired
    GameRepository gameRepository;

    @GetMapping()
    public List<Game> getAllGames(){
        List<Game> gameList = gameRepository.findAll();
        if (gameList.isEmpty() || gameList == null) {
            throw new IllegalArgumentException("No games were found!");
        }
        return gameList;
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Integer id){
        Optional<Game> returnVal = gameRepository.findById(id);
        if(returnVal.isPresent()){
            return returnVal.get();
        } else{
            throw new IllegalArgumentException("No games were found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Game addGame(@RequestBody @Valid Game game){
        if (game==null) throw new IllegalArgumentException("No Game is passed! Game object is null!");
        return gameRepository.save(game);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGame(@RequestBody @Valid Game game){
        //Validate incoming Game Data
        if (game==null)
            throw new IllegalArgumentException("No Game data is passed! Game object is null!");

        //make sure the game exists. and if not, throw exception...
        if (game.getGameId()==null)
            throw new IllegalArgumentException("No such game to update.");

        gameRepository.save(game);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable Integer id){
        Optional<Game> game = gameRepository.findById(id);
        if(game.isPresent()) {
            gameRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No games were found with Id: " + id);
        }
    }

    @GetMapping("/studio/{studio}")
    public List<Game> getGamesByStudio(@PathVariable String studio){
        List<Game> gameList = gameRepository.findAllGamesByStudio(studio);
        if (gameList.isEmpty() || gameList == null) {
            throw new IllegalArgumentException("No games were found with Studio: " + studio);
        }
        return gameList;
    }

    @GetMapping("/esrb/{esrbRating}")
    public List<Game> getGamesByEsrbRating(@PathVariable String esrbRating){
        List<Game> gameList = gameRepository.findAllGamesByEsrbRating(esrbRating);
        if (gameList.isEmpty() || gameList == null) {
            throw new IllegalArgumentException("No games were found with ESRB Rating: " + esrbRating);
        }
        return gameList;
    }

    @GetMapping("/title/{title}")
    public List<Game> getGamesByTitle(@PathVariable String title){
        List<Game> gameList = gameRepository.findAllGamesByTitle(title);
        if (gameList.isEmpty() || gameList == null) {
            throw new IllegalArgumentException("No games were found with Title: " + title);
        }
        return gameList;
    }
}
