package ru.terentyev.rikmasterstesttask.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@Schema(description = "Модель поступления партии кофе")
public class CoffeeInflow extends AbstractEntity {
	
	@Schema(description = "Вес мешка в граммах")
	public static final int BAG_WEIGHT_GRAMS = 60000;
	@Schema(description = "Количество мешков")
	private int bagsCount;
	@Schema(description = "Страна происхождения")
	private String country;
	@Schema(description = "Сорт")
	private String sort;
	@Schema(description = "Процент робусты")
	private double robustaPercentage;
	@Schema(description = "Процент арабики")
	private double arabicaPercentage;
}
