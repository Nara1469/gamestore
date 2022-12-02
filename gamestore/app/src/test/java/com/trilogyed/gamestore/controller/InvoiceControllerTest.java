package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.Invoice;
import com.trilogyed.gamestore.model.TShirt;
import com.trilogyed.gamestore.repository.InvoiceRepository;
import com.trilogyed.gamestore.service.ServiceLayer;
import com.trilogyed.gamestore.viewModel.InvoiceViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceLayer serviceLayer;

    @MockBean
    private InvoiceRepository invoiceRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Invoice invoice;
    private List<Invoice> invoiceList;
    private String inputJson;
    private String outputJson;


    @Before
    public void setUp() throws Exception {
        invoice = new Invoice(45,"Jeff", "123 Main Street", "Tampa", "FL", "12345", "t_shirt", 18, 10.00, 5, 50.00, 3.00, 1.98, 54.98);
        invoiceList = new ArrayList<>();
        invoiceList.add(invoice);
    }

    @Test
    public void shouldReturnAllInvoices() throws Exception {
        doReturn(invoiceList).when(invoiceRepository).findAll();
        outputJson = mapper.writeValueAsString(invoiceList);

        mockMvc.perform(get("/invoice"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnOneInvoiceById() throws Exception  {
        doReturn(Optional.of(invoice)).when(invoiceRepository).findById(45);
        outputJson = mapper.writeValueAsString(invoice);

        mockMvc.perform(get("/invoice/45"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnAllInvoicesByCustomerName() throws Exception  {
        doReturn(invoiceList).when(invoiceRepository).findAllInvoiceByCustomerName("Jeff");
        outputJson = mapper.writeValueAsString(invoiceList);

        mockMvc.perform(get("/invoice/customer/Jeff"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnNewInvoiceAfterSaveInvoice() throws Exception {
        TShirt quantityChangedtTshirt = new TShirt(18, "XS", "white", "Plain", 10.00, 395);

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
        expectedOutput.setId(2);
        expectedOutput.setCustomerName("Jeff");
        expectedOutput.setStreet("123 Main Street");
        expectedOutput.setCity("Tampa");
        expectedOutput.setState("FL");
        expectedOutput.setZipcode("12345");
        expectedOutput.setItemType("T-Shirt");
        expectedOutput.setItemId(18);
        expectedOutput.setItemDetail(quantityChangedtTshirt);
        expectedOutput.setUnitPrice(10.00);
        expectedOutput.setQuantity(5);
        expectedOutput.setSubtotal(50.00);
        expectedOutput.setProcessingFee(1.98);
        expectedOutput.setSalesTax(3.00);
        expectedOutput.setTotal(54.98);

        doReturn(expectedOutput).when(serviceLayer).saveInvoices(inputInvoice);

        inputJson = mapper.writeValueAsString(inputInvoice);
        outputJson = mapper.writeValueAsString(expectedOutput);

        mockMvc.perform(post("/invoice")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }
}