package ru.terentyev.rikmasterstesttask.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coffee")
public class Coffee extends AbstractEntity {
	
	private long grams;
	private String sort;
	private String country;
	private double robustaPercentage;
	private double arabicaPercentage;
}
