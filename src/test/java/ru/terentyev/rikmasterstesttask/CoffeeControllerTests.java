package ru.terentyev.rikmasterstesttask;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.terentyev.rikmasterstesttask.controllers.CoffeeController;
import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;
import ru.terentyev.rikmasterstesttask.services.CoffeeService;

@WebMvcTest(CoffeeController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class CoffeeControllerTests {
	@MockBean
	private CoffeeService coffeeService;
	@InjectMocks
	private CoffeeController coffeeController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private List<CoffeeResponse> stockResponsesList;
    private List<CoffeeResponse> lossesPerBrigadeResponsesList;
    
    
    @BeforeAll
    public void init() {
    	fillStockResponsesList();
    	fillLossesPerBrigadeResponsesList();
    	Mockito.when(coffeeService.takeStock()).thenReturn(stockResponsesList);
    }
    
    public void fillStockResponsesList() {
    	stockResponsesList = new ArrayList<>();
    	
    	CoffeeResponse response1 = new CoffeeResponse();
    	response1.setCountry("USA");
    	response1.setSort("Espresso");
    	response1.setGramsStock(120000);
    	response1.setFreshGramsStock(100000);  	
    	stockResponsesList.add(response1);
    	
    	CoffeeResponse response2 = new CoffeeResponse();
    	response2.setCountry("Australia");
    	response2.setSort("Cappuccino");
    	response2.setGramsStock(180000);
    	response2.setFreshGramsStock(150000);  	
    	stockResponsesList.add(response2);
    	
    	CoffeeResponse response3 = new CoffeeResponse();
    	response3.setCountry("Italy");
    	response3.setSort("Mochaccino");
    	response3.setGramsStock(200000);
    	response3.setFreshGramsStock(70000);  	
    	stockResponsesList.add(response3);
    }
    
    public void fillLossesPerBrigadeResponsesList() {
    	lossesPerBrigadeResponsesList = new ArrayList<>();
    	
    	CoffeeResponse response1 = new CoffeeResponse();
    	response1.setCountry("USA");
    	response1.setSort("Espresso");
    	response1.setLossesPerBrigade(null);
    }
    
    
    @Test
    public void takeStockTest() throws Exception {
    	Mockito.when(coffeeService.takeStock()).thenReturn(stockResponsesList);
    	String expectedJson = objectMapper.writeValueAsString(stockResponsesList);	
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/stock")
    			.accept(MediaType.APPLICATION_JSON_VALUE)
    			.contentType(MediaType.APPLICATION_JSON_VALUE))
    	.andExpect(status().isOk())
    	.andDo(print())
    	.andExpect(content().json(expectedJson));
    }
}
