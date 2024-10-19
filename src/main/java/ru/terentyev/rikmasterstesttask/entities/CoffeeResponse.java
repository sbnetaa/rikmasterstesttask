package ru.terentyev.rikmasterstesttask.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Модель ответа")
public class CoffeeResponse extends AbstractEntity {
	@Schema(description = "Сорт")
	private String sort;
	@Schema(description = "Страна")
	private String country;
	@Schema(description = "Остаток кофе в граммах")
	private Integer gramsStock;
	@Schema(description = "Остаток необжаренного кофе в граммах")
	private Integer freshGramsStock;
	@Schema(description = "Процент потерь для каждой бригады")
	private Map<UUID, Double> lossesPerBrigade = new HashMap<>();
	@Schema(description = "Процент потерь для каждой страны")
	private Map<String, Double> lossesPerCountry = new HashMap<>();
	
}
