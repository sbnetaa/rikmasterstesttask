package ru.terentyev.rikmasterstesttask.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class CoffeeInflow extends AbstractEntity {
	
	public static final int BAG_WEIGHT_GRAMS = 60000;
	private int bagsCount;
	private String country;
	private String sort;
	private double robustaPercentage;
	private double arabicaPercentage;
}
