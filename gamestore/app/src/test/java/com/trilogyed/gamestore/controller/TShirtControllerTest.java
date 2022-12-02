package com.trilogyed.gamestore.controller;

import com.trilogyed.gamestore.model.TShirt;
import com.trilogyed.gamestore.repository.TShirtRepository;
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

import java.util.*;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TShirtController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TShirtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TShirtRepository tshirtRepository;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    private TShirt inputTShirt1;
    private TShirt outputTShirt1;
    private TShirt inputTShirt2;
    private TShirt outputTShirt2;
    private TShirt inputTShirt3;
    private TShirt outputTShirt3;

    private List<TShirt> tshirtList;
    private List<TShirt> tshirtListByColor;
    private List<TShirt> tshirtListBySize;

    private String inputJson;
    private String outputJson;

    @Before
    public void setUp() throws Exception {
        inputTShirt1 = new TShirt(null,"XS", "red", "Plain", 20.00, 100);
        outputTShirt1 = new TShirt(1,"XS", "red", "Plain", 20.00, 100);

        inputTShirt2 = new TShirt(null,"M", "white", "Nike Logo", 30.00, 100);
        outputTShirt2 = new TShirt(31,"M", "white", "Nike Logo", 30.00, 100);

        inputTShirt3 = new TShirt(null,"XS", "white", "Prada", 150.00, 100);
        outputTShirt3 = new TShirt(101,"XS", "white", "Prada", 150.00, 100);

        tshirtList = new ArrayList<>(Arrays.asList(
                outputTShirt1,
                outputTShirt2,
                outputTShirt3
        ));

        tshirtListByColor = new ArrayList<>(Arrays.asList(
                outputTShirt2,
                outputTShirt3
        ));

        tshirtListBySize = new ArrayList<>(Arrays.asList(
                outputTShirt1,
                outputTShirt3
        ));
    }

    // ------------ MockMVC test for successful response ---------------
    // -----------------------------------------------------------------

    @Test
    public void shouldReturnOneTShirtAfterPostMethod() throws Exception {
        doReturn(outputTShirt2).when(tshirtRepository).save(inputTShirt2);

        inputJson = mapper.writeValueAsString(inputTShirt2);
        outputJson = mapper.writeValueAsString(outputTShirt2);

        mockMvc.perform(post("/t-shirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnAllTShirts() throws Exception {
        doReturn(tshirtList).when(tshirtRepository).findAll();
        outputJson = mapper.writeValueAsString(tshirtList);

        mockMvc.perform(get("/t-shirt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnOneTShirt() throws Exception {
        doReturn(Optional.of(outputTShirt2)).when(tshirtRepository).findById(31);
        outputJson = mapper.writeValueAsString(outputTShirt2);

        mockMvc.perform(get("/t-shirt/31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldUpdateTShirtAfterPutMethod() throws Exception {
        inputJson = mapper.writeValueAsString(outputTShirt1);
        doReturn(null).when(tshirtRepository).save(outputTShirt1);

        mockMvc.perform(put("/t-shirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteTShirtAfterDeleteMethod() throws Exception {
        doReturn(Optional.ofNullable(outputTShirt1)).when(tshirtRepository).findById(1);

        mockMvc.perform(
                        delete("/t-shirt/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnAllTShirtsByColor() throws Exception {
        doReturn(tshirtListByColor).when(tshirtRepository).findAllTShirtsByColor("white");
        outputJson = mapper.writeValueAsString(tshirtListByColor);

        mockMvc.perform(get("/t-shirt/color/white"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    public void shouldReturnAllTShirtsBySize() throws Exception {
        doReturn(tshirtListBySize).when(tshirtRepository).findAllTShirtsBySize("XS");
        outputJson = mapper.writeValueAsString(tshirtListBySize);

        mockMvc.perform(get("/t-shirt/size/XS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    // -------------- MockMVC test for invalid request -----------------
    // -----------------------------------------------------------------
    @Test
    public void shouldReturn400ForInvalidInputForTShirtModel() throws Exception {
        Map<String, String> inputMap = new HashMap<>();
        String inputMapJson;

        inputMap.put("size", "XS");
        inputMap.put("color", "red");
        inputMap.put("description", "Plain");
        inputMap.put("quantity", "a");
        inputMap.put("price", "-2.00");

        inputMapJson = mapper.writeValueAsString(inputMap);

        mockMvc.perform(post("/t-shirt")
                        .content(inputMapJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn422ForMissingInputForTShirtModel() throws Exception {
        inputTShirt1.setSize("");
        inputTShirt1.setColor("");
        inputTShirt1.setDescription(null);
        inputTShirt1.setQuantity(null);
        inputTShirt1.setPrice(null);

        inputJson = mapper.writeValueAsString(inputTShirt1);

        doReturn(null).when(tshirtRepository).save(inputTShirt1);

        mockMvc.perform(post("/t-shirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    // ------------ NotFound Status Tests -------------

    @Test
    public void shouldReturn404IWhenGetTShirtByIdNotFound() throws Exception {
        doReturn(Optional.empty()).when(tshirtRepository).findById(15);

        mockMvc.perform(get("/t-shirt/15"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenReadEmptyTShirtTable() throws Exception{
        List<TShirt> emptyList = new ArrayList();

        doReturn(emptyList).when(tshirtRepository).findAll();

        mockMvc.perform(
                        get("/t-shirt")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenUpdateTShirtIdNotFound() throws Exception {
        TShirt anyTShirt = new TShirt();
        anyTShirt.setSize("Med");
        anyTShirt.setColor("Blue");
        anyTShirt.setDescription("Nike");
        anyTShirt.setQuantity(5);
        anyTShirt.setPrice(24.99);

        inputJson = mapper.writeValueAsString(anyTShirt);

        anyTShirt.settShirtId(100);

        doReturn(null).when(tshirtRepository).save(anyTShirt);

        //Act & Assert
        this.mockMvc.perform(put("/t-shirt")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404IWhenDeleteTShirtByIdNotFound() throws Exception {
        doReturn(Optional.ofNullable(null)).when(tshirtRepository).findById(203);

        mockMvc.perform(
                        delete("/t-shirt/203"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenFindByColorEmptyTShirtTable() throws Exception{
        List<TShirt> emptyList = new ArrayList();

        doReturn(emptyList).when(tshirtRepository).findAllTShirtsByColor("Blue");

        mockMvc.perform(
                        get("/t-shirt/color/Blue")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404WhenFindBySizeEmptyTShirtTable() throws Exception{
        List<TShirt> emptyList = new ArrayList();

        doReturn(emptyList).when(tshirtRepository).findAllTShirtsBySize("XXL");

        mockMvc.perform(
                        get("/t-shirt/size/XXL")
                )
                .andExpect(status().isNotFound());
    }
}