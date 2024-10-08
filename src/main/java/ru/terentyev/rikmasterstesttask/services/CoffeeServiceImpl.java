package ru.terentyev.rikmasterstesttask.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Empty;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.terentyev.rikmasterstesttask.entities.Coffee;
import ru.terentyev.rikmasterstesttask.entities.CoffeeInflow;
import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;
import ru.terentyev.rikmasterstesttask.entities.Roasting;
import ru.terentyev.rikmasterstesttask.repositories.CoffeeRepository;
import ru.terentyev.rikmasterstesttask.repositories.RoastingRepository;
import ru.terentyev.rikmasterstesttask.roasting.RoastingRequest;
import ru.terentyev.rikmasterstesttask.roasting.RoastingServiceGrpc;

@Service
@Transactional(readOnly = true)
@GrpcService
public class CoffeeServiceImpl extends RoastingServiceGrpc.RoastingServiceImplBase implements CoffeeService {

	private CoffeeRepository coffeeRepository;
	private RoastingRepository roastingRepository;
	private ObjectMapper objectMapper;
	
	@Autowired
	public CoffeeServiceImpl(CoffeeRepository coffeeRepository, RoastingRepository roastingRepository
			, ObjectMapper objectMapper) {
		super();
		this.coffeeRepository = coffeeRepository;
		this.roastingRepository = roastingRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public List<CoffeeResponse> takeStock() {
		List<CoffeeResponse> responsesList = new ArrayList<>();
		List<Coffee> uniqueCoffeeList = coffeeRepository.findAllGroupBySortAndCountry();
		for (Coffee coffee : uniqueCoffeeList) {
			CoffeeResponse response = new CoffeeResponse();
			String sort = coffee.getSort();
			String country = coffee.getCountry();
			response.setGramsStock(coffeeRepository.takeCommonStockPerSortAndCountry(sort, country));
			response.setFreshGramsStock(coffeeRepository.takeFreshStockPerSortAndCountry(sort, country));
			response.setCountry(country);
			response.setSort(sort);
			responsesList.add(response);
		}
		return responsesList;
	}

	@Override
	public CoffeeResponse takeLossesPerBrigade() {
		CoffeeResponse response = new CoffeeResponse();
		UUID[] brigades = roastingRepository.findAllBrigades();
		for (UUID brigade : brigades) response.getLossesPerBrigade().put(brigade, roastingRepository.takeLossesPerBrigade(brigade));
		return null;
	}

	@Override
	public CoffeeResponse takeLossesPerCountry() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@KafkaListener(topics = "coffee-inflow")
	@Transactional(readOnly = false)
	public void acceptCoffeeInflow(byte[] coffeeInflowAsBytes, Acknowledgment acknowledgment) throws StreamReadException, DatabindException, IOException {
		CoffeeInflow coffeeInflow = objectMapper.readValue(coffeeInflowAsBytes, CoffeeInflow.class);
		Coffee coffee = new Coffee();
		coffee.setGrams(coffeeInflow.getBagsCount() * CoffeeInflow.BAG_WEIGHT_GRAMS);
		coffee.setArabicaPercentage(coffeeInflow.getArabicaPercentage());
		coffee.setRobustaPercentage(coffeeInflow.getRobustaPercentage());
		coffee.setCountry(coffeeInflow.getCountry());
		coffee.setSort(coffeeInflow.getSort());
		coffeeRepository.save(coffee);
		acknowledgment.acknowledge();
	}
	
    @Override
    @Transactional(readOnly = false)
    public void acceptRoasting(RoastingRequest request, StreamObserver<Empty> responseObserver) {
    	String sort = request.getSort();
    	String country = request.getCountry();
    	List<Coffee> coffeeList = coffeeRepository.findAllBySortAndCountry(sort, country);
    	int sumOfFreshGramsPresent = 0;
    	for (Coffee coffee : coffeeList) sumOfFreshGramsPresent += coffee.getGrams() - coffee.getRoastedGramsAtInput();
    	if (coffeeList.isEmpty() || sumOfFreshGramsPresent < request.getGramsBeforeRoasting()) {
			responseObserver.onError(Status.RESOURCE_EXHAUSTED.withDescription("Кофе с сортом " + sort + " и страной " + country + " недостаточно на складе").asException());
		    return;
    	}
    	int gramsToRoastLeft = request.getGramsBeforeRoasting();
    	for (Coffee coffee : coffeeList) {
    		if ((coffee.getGrams() - coffee.getRoastedGramsAtInput()) >= gramsToRoastLeft) {
    			coffee.setRoastedGramsAtInput(coffee.getRoastedGramsAtInput() + request.getGramsBeforeRoasting());
    			break;
    		}
    		int gramsToRoastSingleTmp = coffee.getGrams() - coffee.getRoastedGramsAtInput();
    		coffee.setRoastedGramsAtInput(coffee.getGrams());
    		gramsToRoastLeft -= gramsToRoastSingleTmp;
    	}
    	Roasting roasting = new Roasting();
    	roasting.setGramsTaken(request.getGramsBeforeRoasting());
    	roasting.setSort(request.getSort());
    	roasting.setCountry(request.getCountry());
    	roasting.setGramsResulting(request.getGramsAfterRoasting());
    	roasting.setBrigadeNumber(UUID.fromString(request.getBrigadeNumber()));
    	roastingRepository.save(roasting);
    	coffeeRepository.saveAll(coffeeList);
    	
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

}
