package ru.terentyev.rikmasterstesttask;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.StatusRuntimeException;
import ru.terentyev.rikmasterstesttask.entities.Coffee;
import ru.terentyev.rikmasterstesttask.entities.CoffeeInflow;
import ru.terentyev.rikmasterstesttask.entities.Roasting;
import ru.terentyev.rikmasterstesttask.repositories.CoffeeRepository;
import ru.terentyev.rikmasterstesttask.repositories.RoastingRepository;
import ru.terentyev.rikmasterstesttask.roasting.RoastingRequest;
import ru.terentyev.rikmasterstesttask.roasting.RoastingServiceGrpc;
import ru.terentyev.rikmasterstesttask.services.CoffeeServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
@DirtiesContext
@EnableKafka
class RikmasterstesttaskApplicationTests {
		
		private static final int MAX_BAGS_INFLOW = 10;
		private static final int MAX_KIND_PERCENTAGE = 100;
		//@Autowired
		//private Server gRpcServer;
	    @Autowired
	    private PlatformTransactionManager transactionManager;
	    @InjectMocks
	    @Autowired
	    private CoffeeServiceImpl coffeeService;
	    @Autowired
	    private KafkaTemplate<String, CoffeeInflow> kafkaTemplate;
	    @Autowired
	    private MockMvc mockMvc;
	    @MockBean
	    private CoffeeRepository coffeeRepository;
	    @MockBean
	    private RoastingRepository roastingRepository;
	    private Server server;
	    private TransactionStatus transactionStatus;
	    private ManagedChannel channel;
	    private RoastingServiceGrpc.RoastingServiceBlockingStub blockingStub;
	    private List<CoffeeInflow> coffeeInflowList;
	    private List<RoastingRequest> roastingRequestsList;
	    private List<Coffee> savedCoffee;
	    private List<Roasting> savedRoasting;
	    private Random random = new Random();
	    private List<String> countriesList = List.of("Australia" + Math.abs(random.nextInt(100)), "Spain" + Math.abs(random.nextInt(100)), "Italy" + Math.abs(random.nextInt(100)));
	    private List<String> sortsList = List.of("Espresso" + Math.abs(random.nextInt(100)), "Cappuccino" + Math.abs(random.nextInt(100)), "Mochaccino" + Math.abs(random.nextInt(100)));
	    
	    private int coffeeBagsInflow = Math.abs(random.nextInt(15));
	    
	    @BeforeAll
	    public void setUp() throws IOException {
	    	transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
	        channel = ManagedChannelBuilder.forAddress("localhost", 50051)
	                .usePlaintext()
	                .build();
	        blockingStub = RoastingServiceGrpc.newBlockingStub(channel);
	        
	        server = ServerBuilder.forPort(50051).addService(coffeeService).build().start();
	        
	        fillCoffeeInflowList();
	        fillRoastingRequestsList();
	    }
	    
	    public void fillCoffeeInflowList() {
	    	coffeeInflowList = new ArrayList<>();
	    	
	    	CoffeeInflow coffeeInflow1 = new CoffeeInflow();
	    	coffeeInflow1.setBagsCount(Math.abs(random.nextInt(MAX_BAGS_INFLOW)));
	    	coffeeInflow1.setArabicaPercentage(Math.abs(random.nextDouble(MAX_KIND_PERCENTAGE)));
	    	coffeeInflow1.setRobustaPercentage(Math.abs(random.nextDouble(MAX_KIND_PERCENTAGE - coffeeInflow1.getArabicaPercentage())));
	    	coffeeInflow1.setCountry(countriesList.get(0));
	    	coffeeInflow1.setSort(sortsList.get(0));
	    	
	    	coffeeInflowList.add(coffeeInflow1);
	    	
	    	CoffeeInflow coffeeInflow2 = new CoffeeInflow();
	    	coffeeInflow2.setBagsCount(Math.abs(random.nextInt(MAX_BAGS_INFLOW)));
	    	coffeeInflow2.setArabicaPercentage(Math.abs(random.nextDouble(MAX_KIND_PERCENTAGE)));
	    	coffeeInflow2.setRobustaPercentage(Math.abs(random.nextDouble(MAX_KIND_PERCENTAGE - coffeeInflow2.getArabicaPercentage())));
	    	coffeeInflow2.setCountry(countriesList.get(1));
	    	coffeeInflow2.setSort(sortsList.get(1));
	    	
	    	coffeeInflowList.add(coffeeInflow2);
	    	
	    	
	    	CoffeeInflow coffeeInflow3 = new CoffeeInflow();
	    	coffeeInflow3.setBagsCount(Math.abs(random.nextInt(MAX_BAGS_INFLOW)));
	    	coffeeInflow3.setArabicaPercentage(Math.abs(random.nextDouble(MAX_KIND_PERCENTAGE)));
	    	coffeeInflow3.setRobustaPercentage(Math.abs(random.nextDouble(MAX_KIND_PERCENTAGE - coffeeInflow3.getArabicaPercentage())));
	    	coffeeInflow3.setCountry(countriesList.get(2));
	    	coffeeInflow3.setSort(sortsList.get(2));
	    	
	    	coffeeInflowList.add(coffeeInflow3);
	    }
	    
	    public void fillRoastingRequestsList() {
	    	
	    	roastingRequestsList = new ArrayList<>();
	    	
	    	int freshGramsPresent = coffeeBagsInflow * CoffeeInflow.BAG_WEIGHT_GRAMS;
	    	
	    	int gramsBeforeRoasting1 = Math.abs(random.nextInt(freshGramsPresent));
	    	int gramsAfterRoasting1 = Math.abs(random.nextInt(gramsBeforeRoasting1));
	    	RoastingRequest request1 = RoastingRequest.newBuilder()
	    			.setCountry(countriesList.get(0)).setSort(sortsList.get(0))
	    			.setBrigadeNumber(UUID.randomUUID().toString())
	    			.setGramsBeforeRoasting(gramsBeforeRoasting1)
	    			.setGramsAfterRoasting(gramsAfterRoasting1)
	    			.build();
	    	
	    	roastingRequestsList.add(request1);
	    	
	    	int gramsBeforeRoasting2 = Math.abs(random.nextInt(freshGramsPresent -= gramsBeforeRoasting1));
	    	int gramsAfterRoasting2 = Math.abs(random.nextInt(gramsBeforeRoasting2));
	    	RoastingRequest request2 = RoastingRequest.newBuilder()
	    			.setCountry(countriesList.get(1)).setSort(sortsList.get(1))
	    			.setBrigadeNumber(UUID.randomUUID().toString())
	    			.setGramsBeforeRoasting(gramsBeforeRoasting2)
	    			.setGramsAfterRoasting(gramsAfterRoasting2)
	    			.build();
	    	
	    	roastingRequestsList.add(request2);
	    	
	    	int gramsBeforeRoasting3 = Math.abs(random.nextInt(freshGramsPresent -= gramsBeforeRoasting2));
	    	int gramsAfterRoasting3 = Math.abs(random.nextInt(gramsBeforeRoasting3));
	    	RoastingRequest request3 = RoastingRequest.newBuilder()
	    			.setCountry(countriesList.get(2)).setSort(sortsList.get(2))
	    			.setBrigadeNumber(UUID.randomUUID().toString())
	    			.setGramsBeforeRoasting(gramsBeforeRoasting3)
	    			.setGramsAfterRoasting(gramsAfterRoasting3)
	    			.build();
	    	
	    	roastingRequestsList.add(request3);
	    }
	    
	    @AfterAll
	    public void tearDown() {
	        if (channel != null) {
	            channel.shutdown();
	        }
	        if (transactionStatus != null && !transactionStatus.isCompleted()) {
	            transactionManager.rollback(transactionStatus);
	        }
	    }
	    
	    @Test
	    @Order(1)
	    public void testKafkaConsumer() {
	    	coffeeInflowList.forEach(coffeeInflow -> kafkaTemplate.send("coffee-inflow-topic", coffeeInflow));
	        try {
	            Thread.sleep(6000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        ArgumentCaptor<Coffee> captor = ArgumentCaptor.forClass(Coffee.class);
	        verify(coffeeRepository, times(3)).save(captor.capture());	        
	        savedCoffee = captor.getAllValues();	        
	        assertTrue(savedCoffee.stream().anyMatch(coffee -> coffee.getCountry().equals(coffeeInflowList.get(0).getCountry())));
	        assertTrue(savedCoffee.stream().anyMatch(coffee -> coffee.getSort().equals(coffeeInflowList.get(1).getSort())));
	        assertTrue(savedCoffee.stream().anyMatch(coffee -> coffee.getGrams() == (coffeeInflowList.get(2).getBagsCount() * CoffeeInflow.BAG_WEIGHT_GRAMS)));
	        
	    }
	    
	    @Test
	    @Order(2)
	    public void testRoastingRequests() {	    	
	    	Map<List<String>, Integer> freshGramsStockPerCountryAndSort = new HashMap<>();    	
	    	savedCoffee.forEach(coffee -> {
	    	    freshGramsStockPerCountryAndSort.compute(
	    	        List.of(coffee.getCountry(), coffee.getSort()), 
	    	        (k, v) -> {
	    	        	if (v == null) v = 0;
	    	        	return v += coffee.getGrams() - coffee.getRoastedGramsAtInput();
	    	        }
	    	    );
	    	});
	    	
	    	when(coffeeRepository.findAllBySortAndCountry(anyString(), anyString())).thenAnswer(invocation -> {
	    		List<Coffee> coffeeToReturn = new ArrayList<>();
	    		for (Coffee coffee : savedCoffee) {
	    			if (coffee.getSort().equals(invocation.getArgument(0)) && coffee.getCountry().equals(invocation.getArgument(1)))
	    				coffeeToReturn.add(coffee);
	    		}
	    		return coffeeToReturn;
	    	});
		
	    	int i = 0;
	    	for (RoastingRequest request : roastingRequestsList) {
	    		if (freshGramsStockPerCountryAndSort.get(List.of(request.getCountry(), request.getSort())) < request.getGramsBeforeRoasting()) {
	    			assertThrows(StatusRuntimeException.class, () -> blockingStub.acceptRoasting(request));
	    			i++;
	    		}
	    		else {	    			
	    			blockingStub.acceptRoasting(request);
	    			freshGramsStockPerCountryAndSort.compute(List.of(request.getCountry(), request.getSort()), (k, v) -> {
	    				if (v == null) v = 0;
	    				return v -= request.getGramsBeforeRoasting();
	    			});   			
	    			
	    		}
	    	}

	        ArgumentCaptor<Roasting> captor = ArgumentCaptor.forClass(Roasting.class);
	        verify(roastingRepository, times(roastingRequestsList.size() - i)).save(captor.capture());	        
	        savedRoasting = captor.getAllValues();
      
	        for (Roasting roasting : savedRoasting) {
		        assertTrue(roastingRequestsList.stream().anyMatch(request -> { 
		        	return request.getCountry().equals(roasting.getCountry())
		        	&& request.getSort().equals(roasting.getSort()) && request.getGramsBeforeRoasting() == roasting.getGramsTaken();       	
		        }));
	        }
	        assertTrue(savedRoasting.size() == roastingRequestsList.size() - i);
	 }
	        
}
	    

