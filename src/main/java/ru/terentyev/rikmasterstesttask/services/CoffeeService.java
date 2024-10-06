package ru.terentyev.rikmasterstesttask.services;

import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;

public interface CoffeeService {
	
	CoffeeResponse takeStock(String sort, String country);
	CoffeeResponse takeLossesPerBrigade(String brigadeUuid);
	CoffeeResponse takeLossesPerCountry(String country);
}
