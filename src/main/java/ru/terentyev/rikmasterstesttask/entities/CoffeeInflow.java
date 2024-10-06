package ru.terentyev.rikmasterstesttask.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoffeeInflow extends AbstractEntity {
	
	public static final int BAG_WEIGHT_GRAMS = 60000;
	private int bagsCount;
	private String country;
	private String sort;
	private double robustaPercentage;
	private double arabicaPercentage;
}
