package ru.terentyev.rikmasterstesttask.services;

import java.util.List;

import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;

public interface CoffeeService {
	
	List<CoffeeResponse> takeStock();
	CoffeeResponse takeLossesPerBrigade();
	CoffeeResponse takeLossesPerCountry();
}
