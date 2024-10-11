package ru.terentyev.rikmasterstesttask.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;
import ru.terentyev.rikmasterstesttask.services.CoffeeService;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE
, headers = "Accept=application/json")
public class CoffeeController extends AbstractController {

	private CoffeeService coffeeService;

	public CoffeeController(CoffeeService coffeeService) {
		super();
		this.coffeeService = coffeeService;
	}
	
	@GetMapping("/stock")
	public ResponseEntity<List<CoffeeResponse>> takeStock() {
		return new ResponseEntity<>(coffeeService.takeStock(), HttpStatus.OK);
	}
	
	@GetMapping("/losses/brigade")
	public ResponseEntity<CoffeeResponse> takeLossesPerBrigade() {
		return new ResponseEntity<>(coffeeService.takeLossesPerBrigade(), HttpStatus.OK);
	}
	
	@GetMapping("/losses/country")
	public ResponseEntity<CoffeeResponse> takeLossesPerCountry() {
		return new ResponseEntity<>(coffeeService.takeLossesPerBrigade(), HttpStatus.OK);
	}
	
}
