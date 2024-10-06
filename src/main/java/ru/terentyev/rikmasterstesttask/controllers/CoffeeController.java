package ru.terentyev.rikmasterstesttask.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<CoffeeResponse> takeStock(@RequestParam String sort, @RequestParam String country) {
		return new ResponseEntity<>(coffeeService.takeStock(sort, country), HttpStatus.OK);
	}
	
	@GetMapping("/losses/brigade")
	public ResponseEntity<CoffeeResponse> takeLossesPerBrigade(@RequestParam String brigadeUuid) {
		return new ResponseEntity<>(coffeeService.takeLossesPerBrigade(brigadeUuid), HttpStatus.OK);
	}
	
	@GetMapping("/losses/country")
	public ResponseEntity<CoffeeResponse> takeLossesPerCountry(@RequestParam String country) {
		return new ResponseEntity<>(coffeeService.takeLossesPerBrigade(country), HttpStatus.OK);
	}
	
}
