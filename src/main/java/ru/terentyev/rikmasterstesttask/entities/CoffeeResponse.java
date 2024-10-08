package ru.terentyev.rikmasterstesttask.entities;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CoffeeResponse extends AbstractEntity {

	private String sort;
	private String country;
	private int gramsStock;
	private int freshGramsStock;
	private Map<UUID, Double> lossesPerBrigade;
	private Map<String, Double> lossesPerCountry;
}
