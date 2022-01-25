package com.eqinov.recrutement.consumption;

import java.util.List;

public class YearConsumption {

	private final int year;
	private List<Double> monthValuesMWh;
	
	private double preciseYearValueMWh;
	private long yearValueMWh;
	
	public YearConsumption(int year, List<Double> monthValuesMHz) {
		this.year = year;
		this.monthValuesMWh = monthValuesMHz;
		
		this.preciseYearValueMWh = calculateYearValueMWh();
		this.yearValueMWh = Math.round(getPreciseYearValueMWh());
	}
	
	private double calculateYearValueMWh() {
		return getMonthValuesMWh().stream().mapToDouble(value -> value).sum();
	}
	
	public int getYear() {
		return year;
	}
	
	public List<Double> getMonthValuesMWh() {
		return monthValuesMWh;
	}
	
	public double getPreciseYearValueMWh() {
		return preciseYearValueMWh;
	}
	
	public long getYearValueMWh() {
		return yearValueMWh;
	}
	
}
