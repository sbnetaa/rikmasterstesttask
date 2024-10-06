package ru.terentyev.rikmasterstesttask.services;

import org.springframework.stereotype.Service;

import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;

@Service
public class CoffeeServiceImpl implements CoffeeService {

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

}
