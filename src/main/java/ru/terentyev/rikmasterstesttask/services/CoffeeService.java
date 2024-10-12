package ru.terentyev.rikmasterstesttask.services;

import java.io.IOException;
import java.util.List;

import org.springframework.kafka.support.Acknowledgment;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import ru.terentyev.rikmasterstesttask.entities.CoffeeInflow;
import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;

public interface CoffeeService {
	
	List<CoffeeResponse> takeStock();
	CoffeeResponse takeLossesPerBrigade();
	CoffeeResponse takeLossesPerCountry();
	void acceptCoffeeInflow(CoffeeInflow coffeeInflow, Acknowledgment acknowledgment) throws StreamReadException, DatabindException, IOException;
}
