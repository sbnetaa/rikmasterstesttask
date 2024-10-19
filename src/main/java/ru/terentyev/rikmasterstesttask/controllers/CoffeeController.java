package ru.terentyev.rikmasterstesttask.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ru.terentyev.rikmasterstesttask.entities.CoffeeResponse;
import ru.terentyev.rikmasterstesttask.services.CoffeeService;
import ru.terentyev.rikmasterstesttask.util.SwaggerExamples;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE
, headers = "Accept=application/json")
public class CoffeeController extends AbstractController {

	private CoffeeService coffeeService;

	public CoffeeController(CoffeeService coffeeService) {
		super();
		this.coffeeService = coffeeService;
	}
	
	@Operation(summary = "Остаток кофе" , description = "Возвращает остатки кофе", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject("{}")})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Запрос выполнен", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.STOCK_RESPONSE)))
	    })	
	@GetMapping("/stock")
	public ResponseEntity<List<CoffeeResponse>> takeStock() {
		return new ResponseEntity<>(coffeeService.takeStock(), HttpStatus.OK);
	}
	
	@Operation(summary = "Потери каждой бригады" , description = "Возвращает процент потерь кофе при обжарке для каждой страны", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject("{}")})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Запрос выполнен", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.LOSSES_PER_BRIGADE_RESPONSE)))
	    })	
	@GetMapping("/losses/brigade")
	public ResponseEntity<CoffeeResponse> takeLossesPerBrigade() {
		return new ResponseEntity<>(coffeeService.takeLossesPerBrigade(), HttpStatus.OK);
	}
	
	@Operation(summary = "Потери каждой страны" , description = "Возвращает процент потерь кофе при обжарке для каждой бригады", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject("{}")})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Запрос выполнен", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.LOSSES_PER_COUNTRY_RESPONSE)))
	    })	
	@GetMapping("/losses/country")
	public ResponseEntity<CoffeeResponse> takeLossesPerCountry() {
		return new ResponseEntity<>(coffeeService.takeLossesPerCountry(), HttpStatus.OK);
	}
}
