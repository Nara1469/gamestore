package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.Tax;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TaxRepositoryTest {

    @Autowired
    TaxRepository taxRepository;

    Tax[] states = new Tax[]{
            new Tax("AK",0.06),
            new Tax("AL",0.05),
            new Tax("AR",0.06),
            new Tax("AZ",0.04),
            new Tax("CA",0.06),
            new Tax("CO",0.04),
            new Tax("CT",0.03),
            new Tax("DE",0.05),
            new Tax("FL",0.06),
            new Tax("GA",0.07),
            new Tax("HI",0.05),
            new Tax("IA",0.04),
            new Tax("ID",0.03),
            new Tax("IL",0.05),
            new Tax("IN",0.05),
            new Tax("KS",0.06),
            new Tax("KY",0.04),
            new Tax("LA",0.05),
            new Tax("MA",0.05),
            new Tax("MD",0.07),
            new Tax("ME",0.03),
            new Tax("MI",0.06),
            new Tax("MN",0.06),
            new Tax("MO",0.05),
            new Tax("MS",0.05),
            new Tax("MT",0.03),
            new Tax("NC",0.05),
            new Tax("ND",0.05),
            new Tax("NE",0.04),
            new Tax("NH",0.06),
            new Tax("NJ",0.05),
            new Tax("NM",0.05),
            new Tax("NV",0.04),
            new Tax("NY",0.06),
            new Tax("OH",0.04),
            new Tax("OK",0.04),
            new Tax("OR",0.07),
            new Tax("PA",0.06),
            new Tax("RI",0.06),
            new Tax("SC",0.06),
            new Tax("SD",0.06),
            new Tax("TN",0.05),
            new Tax("TX",0.03),
            new Tax("UT",0.04),
            new Tax("VA",0.06),
            new Tax("VT",0.07),
            new Tax("WA",0.05),
            new Tax("WI",0.03),
            new Tax("WV",0.05),
            new Tax("WY",0.04)
    };

    @Test
    public void getTaxRateObject() {
        Optional<Tax> foundTax;

        for (Tax tax :
                states) {
            foundTax = taxRepository.findById(tax.getState());
            assertTrue(foundTax.isPresent());
            assertEquals(foundTax.get(), tax);
        }
    }

    @Test
    public void getTaxRate() {
        Optional<Tax> foundTax;

        for (Tax tax :
                states) {
            foundTax = taxRepository.findById(tax.getState());
            assertTrue(foundTax.isPresent());
            assertEquals(foundTax.get().getRate(), tax.getRate());
        }
    }
}