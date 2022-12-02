package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.TShirt;
import com.trilogyed.gamestore.repository.TShirtRepository;
import com.trilogyed.gamestore.service.ServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/t-shirt")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TShirtController {

    @Autowired
    TShirtRepository tshirtRepository;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getAllTShirts() {
        List<TShirt> tshirtList = tshirtRepository.findAll();
        if (tshirtList.isEmpty() || tshirtList == null) {
            throw new IllegalArgumentException("T-Shirt data is empty!");
        }
        return tshirtList;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TShirt getTShirtById(@PathVariable Integer id) {
        Optional<TShirt> returnVal = tshirtRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("tShirtId '" + id + "' does not exist");
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TShirt addTShirt(@RequestBody @Valid TShirt tshirt) {
        if (tshirt==null) throw new IllegalArgumentException("No T-Shirt is passed! T-Shirt object is null!");
        return tshirtRepository.save(tshirt);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTShirt(@RequestBody @Valid TShirt tshirt) {
        // validate incoming TShirt data
        if (tshirt==null)
            throw new IllegalArgumentException("No t-shirt data is passed! TShirt object is null!");

        //make sure the tshirt exists. and if not, throw exception...
        if (tshirt.gettShirtId()==null)
            throw new IllegalArgumentException("No such t-shirt to update.");

        tshirtRepository.save(tshirt);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTShirt(@PathVariable Integer id) {
        Optional<TShirt> shirt = tshirtRepository.findById(id);
        if(shirt.isPresent()) {
            tshirtRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("tShirtId '" + id + "' does not exist");
        }
    }

    @GetMapping("/color/{color}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsByColor(@PathVariable String color) {
        List<TShirt> tshirtList = tshirtRepository.findAllTShirtsByColor(color);
        if (tshirtList.isEmpty() || tshirtList == null) {
            throw new IllegalArgumentException("T-Shirt data with " + color + " color is empty!");
        }
        return tshirtList;
    }

    @GetMapping("/size/{size}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsBySize(@PathVariable String size) {
        List<TShirt> tshirtList = tshirtRepository.findAllTShirtsBySize(size);
        if (tshirtList.isEmpty() || tshirtList == null) {
            throw new IllegalArgumentException("T-Shirt data with " + size + " size is empty!");
        }
        return tshirtList;
    }
}
