package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest

public class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;
    Invoice invoice1 = new Invoice();

    @Before
    public void setUp() throws Exception {
        invoiceRepository.deleteAll();

        //Arrange
        invoice1.setCustomerName("Joe Black");
        invoice1.setStreet("123 Main St");
        invoice1.setCity("any City");
        invoice1.setState("NY");
        invoice1.setZipcode("10016");
        invoice1.setItemType("T-Shirts");
        invoice1.setItemId(1);
        invoice1.setUnitPrice(20.00);
        invoice1.setQuantity(2);
        invoice1.setSubtotal(100.00);
        invoice1.setTax(6.00);
        invoice1.setProcessingFee(1.98);
        invoice1.setTotal(107.98);
    }

    @Test
    public void shouldAddFindDeleteInvoice() {

        //Act
        invoice1 = invoiceRepository.save(invoice1);
        Optional<Invoice> invoice2 = invoiceRepository.findById(invoice1.getInvoiceId());

        //Assert
        assertTrue(invoice2.isPresent());
        assertEquals(invoice1, invoice2.get());

        //Act
        invoiceRepository.deleteById(invoice1.getInvoiceId());
        invoice2 = invoiceRepository.findById(invoice1.getInvoiceId());

        //Assert
        assertFalse(invoice2.isPresent());
    }

    @Test
    public void shouldFindByName() {

        //Act
        invoice1 = invoiceRepository.save(invoice1);

        List<Invoice> noInvoice = invoiceRepository.findAllInvoiceByCustomerName("invalidValue");

        List<Invoice> foundOneInvoice = invoiceRepository.findAllInvoiceByCustomerName(invoice1.getCustomerName());

        //Assert
        assertEquals(foundOneInvoice.size(),1);

        assertEquals(noInvoice.size(),0);
    }
}