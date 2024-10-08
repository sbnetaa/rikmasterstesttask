package ru.terentyev.rikmasterstesttask.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.terentyev.rikmasterstesttask.entities.Coffee;
import ru.terentyev.rikmasterstesttask.entities.CoffeeInflow;
import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;
import ru.terentyev.rikmasterstesttask.repositories.CoffeeRepository;

@Service
@Transactional(readOnly = true)
//@GrpcService
public class CoffeeServiceImpl// extends RoastingProcessorGrpc.RoastingProcessorImplBase
implements CoffeeService {

	private CoffeeRepository coffeeRepository;
	private ObjectMapper objectMapper;
	
	@Autowired
	public CoffeeServiceImpl(CoffeeRepository coffeeRepository, ObjectMapper objectMapper) {
		super();
		this.coffeeRepository = coffeeRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public CoffeeResponse takeStock(String sort, String country) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoffeeResponse takeLossesPerBrigade(String brigadeUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoffeeResponse takeLossesPerCountry(String country) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@KafkaListener(topics = "coffee-inflow")
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
	
//    @Override
//    public void sendRoasting(RoastingRequest request, StreamObserver<Empty> responseObserver) {
//    	String sort = request.getSort();
//    	String country = request.getCountry();
//    	List<Coffee> coffeeList = coffeeRepository.findAllBySortAndCountry(sort, country);
//    	int sumOfFreshGramsPresent = 0;
//    	coffeeList.forEach(c -> sumOfFreshGramsPresent += c.getGrams() - c.getRoastedGramsAtInput());
//    	if (coffeeList.isEmpty() || sumOfFreshGramsPresent < request.get) {
//			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Кофе с сортом " + sort + " и страной " + country + " отсутствует на складе").asException());
//		    return;
//    	}
//        responseObserver.onNext(Empty.newBuilder().build());
//        responseObserver.onCompleted();
//    }

}
