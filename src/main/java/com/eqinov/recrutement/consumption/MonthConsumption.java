package com.eqinov.recrutement.consumption;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import com.eqinov.recrutement.data.DataPoint;

/**
 * 
 * Classe représentant les consommations pour un mois donné
 *
 */
public class MonthConsumption {

	private final int monthNumber;
	private final String month;
	private final List<DataPoint> monthPoints;
	private final int pointsPerHour = 6;
	
	private final double preciseMonthValueMWh;
	private final long monthValueMWh;
	
	/*
	 * Constructeur de la classe
	 */
	public MonthConsumption(int monthNumber, List<DataPoint> monthPoints) {
		this.monthNumber = monthNumber;	
		this.month = LocalDate.of(2022, monthNumber, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
		this.monthPoints = monthPoints;

		this.preciseMonthValueMWh = calculateMonthValue();
		this.monthValueMWh = Math.round(getPreciseMonthValueMWh());
	}
	
	/**
	 * Calcule la consommation pour le mois à partir des données.
	 * Chaque point correspond à une puissance en MW utilisée pendant dix minutes (1 heure / 6).
	 * 
	 * E = P x t
	 * 
	 * E : energie consommée (MWh)
	 * P : Puissance (MW)
	 * t : temps (h)
	 * 
	 * @return la consommation mensuelle en MWh
	 */
	private double calculateMonthValue() {
		return getMonthPoints().stream().filter(point -> point.getValue() != null)
				.mapToDouble(point -> point.getValue()).sum() / pointsPerHour;
	}
	
	public int getMonthNumber() {
		return monthNumber;
	}
	
	public String getMonth() {
		return month;
	}
	
	public List<DataPoint> getMonthPoints() {
		return monthPoints;
	}
	
	public double getPreciseMonthValueMWh() {
		return preciseMonthValueMWh;
	}
	
	public long getMonthValueMWh() {
		return monthValueMWh;
	}
	
}
