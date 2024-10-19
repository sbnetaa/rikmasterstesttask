package ru.terentyev.rikmasterstesttask.entities;

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
@Table(name = "coffee")
@Schema(description = "Модель кофе")
public class Coffee extends AbstractEntity {
	
	@Schema(description = "Грамм всего для данной партии кофе")
	private int grams;
	@Schema(description = "Сорт")
	private String sort;
	@Schema(description = "Страна происхождения")
	private String country;
	@Schema(description = "Процент робусты")
	private double robustaPercentage;
	@Schema(description = "Процент арабики")
	private double arabicaPercentage;
	@Schema(description = "Количество грамм, взятых для обжарки")
	private int roastedGramsAtInput;

}
