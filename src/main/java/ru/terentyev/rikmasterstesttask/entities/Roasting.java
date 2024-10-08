package ru.terentyev.rikmasterstesttask.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roasting")
public class Roasting extends AbstractEntity {
	
	private int gramsTaken;
	private String sort;
	private String country;
	private int gramsResulting;
	private UUID brigadeNumber;
}
