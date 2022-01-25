package com.eqinov.recrutement.consumption;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eqinov.recrutement.data.DataPoint;

/**
 * 
 * Répartit les consommations de chaque mois pour une année sélectionnée
 *
 */
public class ConsumptionDispatcher {
	
	private final int year;
	private final List<List<DataPoint>> yearPoints;
	
	private final List<MonthConsumption> monthConsumptions;
	private final List<Double> consumptionValuesMHz;
	private final YearConsumption yearConsumption;

	private final int decemberIndex = 12;

	/*
	 * Constructeur de la classe
	 */
	public ConsumptionDispatcher(Integer currentYear, List<List<DataPoint>> yearPoints) {
		this.year = currentYear;
		this.yearPoints = yearPoints;
		
		this.monthConsumptions = dispatchToMonthConsumptions();
		this.consumptionValuesMHz = collectConsumptionValues();
		this.yearConsumption = new YearConsumption(currentYear, getConsumptionValuesMHz());
	}
	
	/**
	 * Répartit les consommations pour chaque mois
	 * @return
	 */
	private List<MonthConsumption> dispatchToMonthConsumptions() {
		List<MonthConsumption> monthConsumptions = new ArrayList<>();
		for (int monthNum = 0 ; monthNum < decemberIndex ; monthNum++ ) {
			List<DataPoint> monthPoints = getYearPoints().get(monthNum);
			monthConsumptions.add(new MonthConsumption(monthNum + 1, monthPoints));
		}
		return monthConsumptions;
	}
	
	/**
	 * Récupère le résultat des consommations mensuelles
	 * @return
	 */
	private List<Double> collectConsumptionValues() {
		List<MonthConsumption> monthConsumptions = getMonthConsumptions();
		return monthConsumptions.stream().map(month -> month.getPreciseMonthValueMWh())
				.collect(Collectors.toList());
	}
	

	// Accesseurs et mutateurs de la classe

	public int getYear() {
		return year;
	}
	
	public List<List<DataPoint>> getYearPoints() {
		return yearPoints;
	}
	
	public List<MonthConsumption> getMonthConsumptions() {
		return monthConsumptions;
	}
	
	public List<Double> getConsumptionValuesMHz() {
		return consumptionValuesMHz;
	}
	
	public YearConsumption getYearConsumption() {
		return yearConsumption;
	}

}
