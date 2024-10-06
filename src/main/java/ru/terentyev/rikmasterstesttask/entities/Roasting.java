package ru.terentyev.rikmasterstesttask.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Roasting extends AbstractEntity {
	
	private int gramsTaken;
	private String sort;
	private String country;
	private int gramsResulting;
	private UUID brigadeNumber;
}
