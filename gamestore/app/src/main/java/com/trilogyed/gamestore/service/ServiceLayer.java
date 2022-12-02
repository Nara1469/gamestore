package com.trilogyed.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.trilogyed.gamestore.repository.*;
import com.trilogyed.gamestore.model.*;
import com.trilogyed.gamestore.viewModel.InvoiceViewModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ServiceLayer {

    private final Double ADDITIONAL_FEE = 15.49;
    private final Double MAX_INVOICE_TOTAL = 999.99;
    private final String GAME_ITEM_TYPE = "Game";
    private final String CONSOLE_ITEM_TYPE = "Console";
    private final String TSHIRT_ITEM_TYPE = "T-Shirt";

    private GameRepository gameRepository;
    private ConsoleRepository consoleRepository;
    private TShirtRepository tShirtRepository;
    private InvoiceRepository invoiceRepository;
    private TaxRepository taxRepository;
    private FeeRepository feeRepository;

    @Autowired
    public ServiceLayer(GameRepository gameRepository, ConsoleRepository consoleRepository, TShirtRepository tShirtRepository, InvoiceRepository invoiceRepository, TaxRepository taxRepository, FeeRepository feeRepository) {
        this.gameRepository = gameRepository;
        this.consoleRepository = consoleRepository;
        this.tShirtRepository = tShirtRepository;
        this.invoiceRepository = invoiceRepository;
        this.taxRepository = taxRepository;
        this.feeRepository = feeRepository;
    }

    @Transactional
    public InvoiceViewModel saveInvoices(Invoice invoice){

        //validation...
        if (invoice==null)
            throw new NullPointerException("Create invoice failed. no invoice data.");

        if(invoice.getItemType()==null)
            throw new IllegalArgumentException("Unrecognized Item type. Valid ones: Console or Game");

        //Check Quantity is > 0...
        if(invoice.getQuantity()<=0){
            throw new IllegalArgumentException(invoice.getQuantity() + ": Unrecognized Quantity. Must be > 0.");
        }

        //start building invoice...
        Invoice newInvoice = new Invoice();
        newInvoice.setCustomerName(invoice.getCustomerName());
        newInvoice.setStreet(invoice.getStreet());
        newInvoice.setCity(invoice.getCity());
        newInvoice.setState(invoice.getState());
        newInvoice.setZipcode(invoice.getZipcode());
        newInvoice.setItemType(invoice.getItemType());
        newInvoice.setItemId(invoice.getItemId());

        // Set quantity and unitPrice based on itemType
        if(newInvoice.getItemType().equalsIgnoreCase(GAME_ITEM_TYPE)) {
            // set it to the text as it appears in the DB
            newInvoice.setItemType(GAME_ITEM_TYPE);
            Optional<Game> game = gameRepository.findById(newInvoice.getItemId());
            if (game.isPresent()) {
                if (game.get().getQuantity() >= invoice.getQuantity()){
                    newInvoice.setQuantity(invoice.getQuantity());
                } else{
                    newInvoice.setQuantity(game.get().getQuantity());
                }
                // Updating quantity in Game
                game.get().setQuantity(game.get().getQuantity() - newInvoice.getQuantity());
                newInvoice.setUnitPrice(game.get().getPrice());
            } else {
                throw new IllegalArgumentException("gameId '" + newInvoice.getItemId() + "' does not exist.");
            }
        } else if(newInvoice.getItemType().equalsIgnoreCase(CONSOLE_ITEM_TYPE)) {
            // set it to the text as it appears in the DB
            newInvoice.setItemType(CONSOLE_ITEM_TYPE);
            Optional<Console> console = consoleRepository.findById(newInvoice.getItemId());
            if (console.isPresent()) {
                if (console.get().getQuantity() >= invoice.getQuantity()){
                    newInvoice.setQuantity(invoice.getQuantity());
                } else{
                    newInvoice.setQuantity(console.get().getQuantity());
                }
                // Updating quantity in Game
                console.get().setQuantity(console.get().getQuantity() - newInvoice.getQuantity());
                newInvoice.setUnitPrice(console.get().getPrice());
            } else {
                throw new IllegalArgumentException("consoleId '" + newInvoice.getItemId() + "' does not exist.");
            }
        } else if(newInvoice.getItemType().equalsIgnoreCase(TSHIRT_ITEM_TYPE)) {
            // set it to the text as it appears in the DB
            newInvoice.setItemType(TSHIRT_ITEM_TYPE);
            Optional<TShirt> tshirt = tShirtRepository.findById(invoice.getItemId());
            if (tshirt.isPresent()) {
                if (tshirt.get().getQuantity() >= invoice.getQuantity()){
                    newInvoice.setQuantity(invoice.getQuantity());
                } else{
                    newInvoice.setQuantity(tshirt.get().getQuantity());
                }
                // Updating quantity in T-Shirt
                tshirt.get().setQuantity(tshirt.get().getQuantity() - newInvoice.getQuantity());
                newInvoice.setUnitPrice(tshirt.get().getPrice());
            } else {
                throw new IllegalArgumentException("tshirtId '" + newInvoice.getItemId() + "' does not exist.");
            }
        } else {
            throw new IllegalArgumentException(invoice.getItemType() + ": Unrecognized Item type. Valid ones: T-Shirt, Console, or Game");
        }

        // Calculating Subtotal, Tax, ProcessingFee and Total
        newInvoice.setSubtotal(newInvoice.getUnitPrice() * newInvoice.getQuantity());
        //Throw Exception if subtotal is greater than 999.99
        if ((newInvoice.getSubtotal().compareTo(MAX_INVOICE_TOTAL) > 0)) {
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }

        Tax tax = taxRepository.findByState(invoice.getState());
        newInvoice.setTax(tax.getRate() * newInvoice.getSubtotal());

        Fee fee = feeRepository.findByProductType(invoice.getItemType());
        if (newInvoice.getQuantity() > 10) {
            newInvoice.setProcessingFee(fee.getFee() + ADDITIONAL_FEE);
        } else {
            newInvoice.setProcessingFee(fee.getFee());
        }

        newInvoice.setTotal(newInvoice.getSubtotal() + newInvoice.getTax() + newInvoice.getProcessingFee());

        invoiceRepository.save(newInvoice);

        return buildInvoiceViewModel(newInvoice);
    }

    private InvoiceViewModel buildInvoiceViewModel(Invoice invoice){
        InvoiceViewModel invoiceView = new InvoiceViewModel();
        invoiceView.setId(invoice.getInvoiceId());
        invoiceView.setCustomerName(invoice.getCustomerName());
        invoiceView.setStreet(invoice.getStreet());
        invoiceView.setCity(invoice.getCity());
        invoiceView.setState(invoice.getState());
        invoiceView.setZipcode(invoice.getZipcode());
        invoiceView.setItemType(invoice.getItemType());
        invoiceView.setItemId(invoice.getItemId());

        // Set itemDetail based on itemType
        Integer itemId = invoice.getItemId();
        if (invoice.getItemType().equals(GAME_ITEM_TYPE)) {
            Optional<Game> game = gameRepository.findById(itemId);
            if (game.isPresent()) {
                invoiceView.setItemDetail(game.get());
            }
        } else if (invoice.getItemType().equals(CONSOLE_ITEM_TYPE)) {
            Optional<Console> console = consoleRepository.findById(itemId);
            if (console.isPresent()) {
                invoiceView.setItemDetail(console.get());
            }
        } else if (invoice.getItemType().equals(TSHIRT_ITEM_TYPE)) {
            Optional<TShirt> tshirt = tShirtRepository.findById(itemId);
            if (tshirt.isPresent()) {
                invoiceView.setItemDetail(tshirt.get());
            }
        }

        invoiceView.setUnitPrice(invoice.getUnitPrice());
        invoiceView.setQuantity(invoice.getQuantity());
        invoiceView.setSubtotal(invoice.getSubtotal());
        invoiceView.setSalesTax(invoice.getTax());
        invoiceView.setProcessingFee(invoice.getProcessingFee());
        invoiceView.setTotal(invoice.getTotal());

        return invoiceView;
    }
}