package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.Console;
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
public class ConsoleRepositoryTest {

    @Autowired
    ConsoleRepository consoleRepository;

    @Before
    public void setUp() throws Exception {
        consoleRepository.deleteAll();
    }

    @Test
    public void shouldAddFindDeleteConsole() {

        //Arrange
        Console newConsole = new Console();
        newConsole.setQuantity(1);
        newConsole.setPrice(10.05);
        newConsole.setProcessor("AMD");
        newConsole.setMemoryAmount("2GB");
        newConsole.setManufacturer("Sega");
        newConsole.setModel("P3");

        //Act
        newConsole = consoleRepository.save(newConsole);
        Optional<Console> foundConsole = consoleRepository.findById(newConsole.getConsoleId());

        //Assert
        assertTrue(foundConsole.isPresent());
        assertEquals(newConsole, foundConsole.get());

        //Arrange
        newConsole.setQuantity(5);
        newConsole.setProcessor("Intel");

        //Act
        consoleRepository.save(newConsole);
        foundConsole = consoleRepository.findById(newConsole.getConsoleId());

        //Assert
        assertTrue(foundConsole.isPresent());
        assertEquals(newConsole, foundConsole.get());

        //Act
        consoleRepository.deleteById(newConsole.getConsoleId());
        foundConsole = consoleRepository.findById(newConsole.getConsoleId());

        //Assert
        assertFalse(foundConsole.isPresent());
    }

    @Test
    public void shouldFindAllConsole(){
        //Arrange
        Console newConsole1 = new Console();
        newConsole1.setQuantity(1);
        newConsole1.setPrice(10.05);
        newConsole1.setProcessor("AMD");
        newConsole1.setMemoryAmount("2GB");
        newConsole1.setManufacturer("Sega");
        newConsole1.setModel("P3");

        Console newConsole2 = new Console();
        newConsole2.setQuantity(5);
        newConsole2.setPrice(11.05);
        newConsole2.setProcessor("AMD");
        newConsole2.setMemoryAmount("2GB");
        newConsole2.setManufacturer("PS-IV");
        newConsole2.setModel("P3");

        //Act
        newConsole1 = consoleRepository.save(newConsole1);
        newConsole2 = consoleRepository.save(newConsole2);
        List<Console> allConsole = new ArrayList();
        allConsole.add(newConsole1);
        allConsole.add(newConsole2);

        //Act
        List<Console> foundAllConsole = consoleRepository.findAll();

        //Assert
        assertEquals(allConsole.size(),foundAllConsole.size());
    }

    @Test
    public void shouldFindConsoleByManufacturer(){
        //Arrange
        Console newConsole1 = new Console();
        newConsole1.setQuantity(1);
        newConsole1.setPrice(10.05);
        newConsole1.setProcessor("AMD");
        newConsole1.setMemoryAmount("2GB");
        newConsole1.setManufacturer("Sega");
        newConsole1.setModel("P3");

        Console newConsole2 = new Console();
        newConsole2.setQuantity(5);
        newConsole2.setPrice(11.05);
        newConsole2.setProcessor("AMD");
        newConsole2.setMemoryAmount("2GB");
        newConsole2.setManufacturer("PS-IV");
        newConsole2.setModel("P3");

        Console newConsole3 = new Console();
        newConsole3.setQuantity(5);
        newConsole3.setPrice(41.05);
        newConsole3.setProcessor("Intel");
        newConsole3.setMemoryAmount("8GB");
        newConsole3.setManufacturer("PS-IV");
        newConsole3.setModel("P3");

        //Act
        newConsole1 = consoleRepository.save(newConsole1);
        newConsole2 = consoleRepository.save(newConsole2);
        newConsole3 = consoleRepository.save(newConsole3);

        //Act
        List<Console> foundNoConsole = consoleRepository.findAllConsolesByManufacturer("InvalidManufacturer");

        List<Console> foundOneConsole = consoleRepository.findAllConsolesByManufacturer("Sega");

        List<Console> foundTwoConsole = consoleRepository.findAllConsolesByManufacturer("PS-IV");

        //Assert
        assertEquals(foundNoConsole.size(),0);
        assertEquals(foundOneConsole.size(),1);
        assertEquals(foundTwoConsole.size(),2);
    }
}