package ru.terentyev.rikmasterstesttask;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.core.type.TypeReference;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.terentyev.rikmasterstesttask.entities.Coffee;
import ru.terentyev.rikmasterstesttask.entities.CoffeeInflow;
import ru.terentyev.rikmasterstesttask.repositories.CoffeeRepository;
import ru.terentyev.rikmasterstesttask.roasting.RoastingRequest;
import ru.terentyev.rikmasterstesttask.roasting.RoastingServiceGrpc;
import ru.terentyev.rikmasterstesttask.services.CoffeeService;
import ru.terentyev.rikmasterstesttask.services.CoffeeServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
@DirtiesContext
@EnableKafka
class RikmasterstesttaskApplicationTests {
		
		private static final int MAX_BAGS_INFLOW = 10;
		private static final int MAX_KIND_PERCENTAGE = 100;
		@Autowired
		private Server gRpcServer;
	    @Autowired
	    private PlatformTransactionManager transactionManager;
	    @InjectMocks
	    @Autowired
	    private CoffeeService coffeeService;
	    @Autowired
	    private KafkaTemplate<String, CoffeeInflow> kafkaTemplate;
	    @Autowired
	    private MockMvc mockMvc;
	    @MockBean
	    private CoffeeRepository coffeeRepository;
	    private TransactionStatus transactionStatus;
	    private ManagedChannel channel;
	    private RoastingServiceGrpc.RoastingServiceBlockingStub blockingStub;
	    private List<CoffeeInflow> coffeeInflowList;
	    private List<RoastingRequest> roastingRequestsList;
	    private List<Coffee> coffeeList;
	    private Random random;
	    private List<String> countriesList = List.of("Australia" + random.nextInt(), "Spain" + random.nextInt(), "Italy" + random.nextInt());
	    private List<String> sortsList = List.of("Espresso" + random.nextInt(), "Cappuccino" + random.nextInt(), "Mochaccino" + random.nextInt());
	    
	    private int coffeeBagsInflow;
	    
	    @BeforeAll
	    public void setUp() {
	    	transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
	        channel = ManagedChannelBuilder.forAddress("localhost", 50051)
	                .usePlaintext()
	                .build();
	        blockingStub = RoastingServiceGrpc.newBlockingStub(channel);
	        
	        server = ServerBuilder.forPort(50051)
	                .addService(coffeeService)
	                .build()
	                .start();
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
	    void testKafkaConsumer() {
	    	coffeeInflowList.forEach(coffeeInflow -> kafkaTemplate.send("coffee-inflow-topic", coffeeInflow));	        
	        ArgumentCaptor<Coffee> captor = ArgumentCaptor.forClass(Coffee.class);
	        verify(coffeeRepository, times(3)).save(captor.capture());	        
	        List<Coffee> savedCoffee = captor.getAllValues();	        
	        assertTrue(savedCoffee.stream().anyMatch(coffee -> coffee.getCountry().equals(coffeeInflowList.get(0).getCountry())));
	        assertTrue(savedCoffee.stream().anyMatch(coffee -> coffee.getSort().equals(coffeeInflowList.get(1).getSort())));
	        assertTrue(savedCoffee.stream().anyMatch(coffee -> coffee.getGrams() == (coffeeInflowList.get(2).getBagsCount() * CoffeeInflow.BAG_WEIGHT_GRAMS)));


	    }
	    
	    @Test
	    public void testGetCoffee() {
	        RoastingRequest request = RoastingRequest.newBuilder()
	                .setCountry("Australia").setSort("Espresso")	                
	                .build();

	        Coffee response = blockingStub.getCoffee(request);

	        assertEquals("Espresso", response.getName());
	        assertEquals(100, response.getGrams());
	    }
}
