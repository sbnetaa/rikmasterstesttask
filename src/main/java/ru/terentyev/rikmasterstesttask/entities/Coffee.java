package ru.terentyev.rikmasterstesttask.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "coffee")
public class Coffee extends AbstractEntity {
	
	private int grams;
	private String sort;
	private String country;
	private double robustaPercentage;
	private double arabicaPercentage;
	private int roastedGramsAtInput;

}
