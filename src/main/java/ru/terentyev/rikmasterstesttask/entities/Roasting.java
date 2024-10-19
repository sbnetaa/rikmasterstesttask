package ru.terentyev.rikmasterstesttask.entities;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Модель обжарки кофе")
public class Roasting extends AbstractEntity {
	
	@Schema(description = "Количество грам, взятых из Coffee для обжарки")
	private int gramsTaken;
	@Schema(description = "Сорт взятого кофе")
	private String sort;
	@Schema(description = "Страна происхождения взятого кофе")
	private String country;
	@Schema(description = "Количество грам на выходе после обжарки")
	private int gramsResulting;
	@Schema(description = "Номер бригады")
	private UUID brigadeNumber;
	@Schema(description = "Процент потерь")
	private double lossesPercentage;
}
