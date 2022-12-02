package com.trilogyed.gamestore.service;

import com.trilogyed.gamestore.repository.*;
import com.trilogyed.gamestore.model.*;
import com.trilogyed.gamestore.viewModel.InvoiceViewModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServiceLayerTest {

    private GameRepository gameRepository;
    private ConsoleRepository consoleRepository;
    private TShirtRepository tshirtRepository;
    private InvoiceRepository invoiceRepository;
    private TaxRepository taxRepository;
    private FeeRepository feeRepository;
    private ServiceLayer serviceLayer;

    @Before
    public void setUp() throws Exception {
        setUpGameRepositoryMock();
        setUpConsoleRepositoryMock();
        setUpTshirtRepositoryMock();
        setUpInvoiceRepositoryMock();
        setUpTaxRepositoryMock();
        setUpFeeRepositoryMock();

        serviceLayer = new ServiceLayer(gameRepository, consoleRepository, tshirtRepository, invoiceRepository, taxRepository, feeRepository);
    }

    public void setUpGameRepositoryMock() {
        gameRepository = mock(GameRepository.class);
        Game game = new Game(4, "Zelda", "rated E", "Nintendo Switch", "Nintendo", 50.00, 10);
        Game game2 = new Game(null,"Zelda", "rated E", "Nintendo Switch", "Nintendo", 50.00, 10);
        List<Game> gameList = new ArrayList<>();
        gameList.add(game);

        doReturn(game).when(gameRepository).save(game2);
        doReturn(Optional.of(game)).when(gameRepository).findById(4);
        doReturn(gameList).when(gameRepository).findAll();
    }

    public void setUpConsoleRepositoryMock() {
        consoleRepository = mock(ConsoleRepository.class);
        Console console = new Console(10, "Xbox", "Sony", "3T", "Intel", 300.00, 50);
        Console console2 = new Console(null,"Xbox", "Sony", "3T", "Intel", 300.00, 50);
        List<Console> consoleList = new ArrayList<>();
        consoleList.add(console);

        doReturn(console).when(consoleRepository).save(console2);
        doReturn(Optional.of(console)).when(consoleRepository).findById(10);
        doReturn(consoleList).when(consoleRepository).findAll();
    }

    public void setUpTshirtRepositoryMock() {
        tshirtRepository = mock(TShirtRepository.class);
        TShirt tshirt = new TShirt(18, "XS", "white", "Plain", 10.00, 400);
        TShirt tshirt2 = new TShirt(null, "XS", "white", "Plain", 10.00, 400);
        List<TShirt> tshirtList = new ArrayList<>();
        tshirtList.add(tshirt);

        doReturn(tshirt).when(tshirtRepository).save(tshirt2);
        doReturn(Optional.of(tshirt)).when(tshirtRepository).findById(18);
        doReturn(tshirtList).when(tshirtRepository).findAll();
    }

    public void setUpInvoiceRepositoryMock() {
        invoiceRepository = mock(InvoiceRepository.class);
        Invoice invoice = new Invoice(2,"Jeff", "123 Main Street", "Tampa", "FL", "12345", "t_shirt", 18, 10.00, 5, 50.00, 3.00, 1.98, 54.98);
        Invoice invoice2 = new Invoice(null,"Jeff", "123 Main Street", "Tampa", "FL", "12345", "t_shirt", 18, 10.00, 5, 50.00, 3.00, 1.98, 54.98);
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);

        doReturn(invoice).when(invoiceRepository).save(invoice2);
        doReturn(Optional.of(invoice)).when(invoiceRepository).findById(2);
        doReturn(invoiceList).when(invoiceRepository).findAll();
    }

    public void setUpTaxRepositoryMock() {
        taxRepository = mock(TaxRepository.class);
        Tax tax = new Tax("FL", 0.06);

        List<Tax> taxList = new ArrayList<>();
        taxList.add(tax);

        doReturn(taxList).when(taxRepository).findAll();
        doReturn(tax).when(taxRepository).findByState("FL");
    }

    public void setUpFeeRepositoryMock() {
        feeRepository = mock(FeeRepository.class);
        Fee fee = new Fee("T-Shirt", 1.98);

        List<Fee> feeList = new ArrayList<>();
        feeList.add(fee);

        doReturn(feeList).when(feeRepository).findAll();
        doReturn(fee).when(feeRepository).findByProductType("T-Shirt");
    }

    @Test
    public void shouldSaveInvoice() throws Exception {
        // Arrange
        TShirt quantityChangedtTShirt = new TShirt(18, "XS", "white", "Plain", 10.00, 395);

        Invoice inputInvoice = new Invoice();
        inputInvoice.setCustomerName("Jeff");
        inputInvoice.setStreet("123 Main Street");
        inputInvoice.setCity("Tampa");
        inputInvoice.setState("FL");
        inputInvoice.setZipcode("12345");
        inputInvoice.setItemType("T-Shirt");
        inputInvoice.setItemId(18);
        inputInvoice.setQuantity(5);

        InvoiceViewModel expectedOutput = new InvoiceViewModel();
        expectedOutput.setCustomerName("Jeff");
        expectedOutput.setStreet("123 Main Street");
        expectedOutput.setCity("Tampa");
        expectedOutput.setState("FL");
        expectedOutput.setZipcode("12345");
        expectedOutput.setItemType("T-Shirt");
        expectedOutput.setItemId(18);
        expectedOutput.setItemDetail(quantityChangedtTShirt);
        expectedOutput.setUnitPrice(10.00);
        expectedOutput.setQuantity(5);
        expectedOutput.setSubtotal(50.00);
        expectedOutput.setProcessingFee(1.98);
        expectedOutput.setSalesTax(3.00);
        expectedOutput.setTotal(54.98);

        // Act
        InvoiceViewModel actualInvoiceViewModel = serviceLayer.saveInvoices(inputInvoice);

        // Assert
        assertEquals(expectedOutput, actualInvoiceViewModel);
    }
}