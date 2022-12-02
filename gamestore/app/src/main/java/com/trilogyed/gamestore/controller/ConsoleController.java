package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.Console;
import com.trilogyed.gamestore.repository.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/console")
@CrossOrigin(origins = {"http://localhost:3000"})
public class ConsoleController {

    @Autowired
    private ConsoleRepository consoleRepository;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Console> getAllConsoles() {
        return consoleRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Console getConsoleById(@PathVariable Integer id) {
        Optional<Console> console = consoleRepository.findById(id);
        if(console.isPresent()) {
            return console.get();
        } else {
            throw new IllegalArgumentException("consoleId '" + id + "' does not exist");
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Console addConsole(@RequestBody Console console) {
        return consoleRepository.save(console);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateConsole(@RequestBody Console console) {
        consoleRepository.save(console);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsole(@PathVariable Integer id){
        Optional<Console> console = consoleRepository.findById(id);
        if(console.isPresent()) {
            consoleRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("consoleId '" + id + "' does not exist");
        }
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public List<Console> getConsolesByManufacturer(@PathVariable String manufacturer){
        return consoleRepository.findAllConsolesByManufacturer(manufacturer);
    }
}
