package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.Invoice;
import com.trilogyed.gamestore.repository.InvoiceRepository;
import com.trilogyed.gamestore.service.ServiceLayer;
import com.trilogyed.gamestore.viewModel.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/invoice")
@CrossOrigin(origins = {"http://localhost:3000"})
public class InvoiceController {

    private InvoiceRepository invoiceRepository;
    private ServiceLayer serviceLayer;

    @Autowired
    public InvoiceController(InvoiceRepository invoiceRepository, ServiceLayer serviceLayer) {
        this.invoiceRepository = invoiceRepository;
        this.serviceLayer = serviceLayer;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceViewModel addInvoice(@RequestBody @Valid Invoice invoice){
        return serviceLayer.saveInvoices(invoice);
    }

    @GetMapping()
    public List<Invoice> getAllInvoices(){
        return invoiceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Integer id){
        Optional<Invoice> returnVal = invoiceRepository.findById(id);
        if(returnVal.isPresent()){
            return returnVal.get();
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invoiceId '" + id + "' does not exist");
        }
    }

    @GetMapping("/customer/{customerName}")
    public List<Invoice> getInvoicesByCustomerName(@PathVariable String customerName){
        return invoiceRepository.findAllInvoiceByCustomerName(customerName);
    }
}
