package ru.terentyev.rikmasterstesttask.entities;

import java.util.UUID;

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
@Table(name = "roasting")
public class Roasting extends AbstractEntity {
	
	private int gramsTaken;
	private String sort;
	private String country;
	private int gramsResulting;
	private UUID brigadeNumber;
	private double lossesPercentage;
}
