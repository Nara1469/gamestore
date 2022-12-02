package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.Fee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FeeRepositoryTest {

    @Autowired
    FeeRepository feeRepository;

    @Before
    public void setUp() throws Exception {
        feeRepository.deleteAll();
    }

    @Test
    public void getFeeObject() {
        // Arrange
        Fee tShirtFee = new Fee();
        tShirtFee.setProductType("T-Shirts");
        tShirtFee.setFee(1.98);

        Fee consoleFee = new Fee();
        consoleFee.setProductType("Consoles");
        consoleFee.setFee(14.99);

        Fee gameFee = new Fee();
        gameFee.setProductType("Games");
        gameFee.setFee(1.49);

        // Act
        feeRepository.save(tShirtFee);
        feeRepository.save(consoleFee);
        feeRepository.save(gameFee);

        // Assert
        Optional<Fee> foundFee = feeRepository.findById(tShirtFee.getProductType());
        assertTrue(foundFee.isPresent());
        assertEquals(tShirtFee, foundFee.get());

        foundFee = feeRepository.findById(consoleFee.getProductType());
        assertTrue(foundFee.isPresent());
        assertEquals(consoleFee, foundFee.get());

        foundFee = feeRepository.findById(gameFee.getProductType());
        assertTrue(foundFee.isPresent());
        assertEquals(gameFee,foundFee.get());
    }

    @Test
    public void getFee() throws  Exception{
        // Arrange
        Fee tShirtFee = new Fee();
        tShirtFee.setProductType("T-Shirt");
        tShirtFee.setFee(1.98);

        Fee consoleFee = new Fee();
        consoleFee.setProductType("Console");
        consoleFee.setFee(14.99);

        Fee gameFee = new Fee();
        gameFee.setProductType("Game");
        gameFee.setFee(1.49);

        // Act
        feeRepository.save(tShirtFee);
        feeRepository.save(consoleFee);
        feeRepository.save(gameFee);

        // Assert
        Optional<Fee> foundFee;
        Double expectedFee;

        foundFee = feeRepository.findById("T-Shirt");
        assertTrue(foundFee.isPresent());
        expectedFee = foundFee.get().getFee();
        assertEquals(expectedFee, tShirtFee.getFee());

        foundFee = feeRepository.findById("Console");
        assertTrue(foundFee.isPresent());
        assertEquals(foundFee.get().getFee(), consoleFee.getFee());

        foundFee = feeRepository.findById("Game");
        assertTrue(foundFee.isPresent());
        assertEquals(foundFee.get().getFee(), gameFee.getFee());
    }
}
