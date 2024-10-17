package ru.terentyev.rikmasterstesttask.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CoffeeResponse extends AbstractEntity {
	private String sort;
	private String country;
	private int gramsStock;
	private int freshGramsStock;
	private Map<UUID, Double> lossesPerBrigade = new HashMap<>();
	private Map<String, Double> lossesPerCountry = new HashMap<>();
	
}
