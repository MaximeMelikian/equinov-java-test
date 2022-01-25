package com.eqinov.test.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.eqinov.recrutement.data.Address;
import com.eqinov.recrutement.data.DataPoint;
import com.eqinov.recrutement.data.Site;

/**
 * 
 * Class to convert JSONobject to Java Objects Site and List<DataPoints>
 *
 */
public class JSONObjectDataConvertor {

	private Site site = new Site();
	private List<DataPoint> consos = new ArrayList<DataPoint>();

	/**
	 * Class constructor, take converted Json object as input
	 * 
	 */
	public JSONObjectDataConvertor(JSONObject jsonObjectImport) {

		this.site = buildSite(jsonObjectImport);
		this.consos = collectConsos(jsonObjectImport);
		getSite().setConsos(getConsos());

	}

	/**
	 * Initialise Site object from data
	 * 
	 * @param jsonObjectImport
	 * @return
	 */
	private Site buildSite(JSONObject jsonObjectImport) {
		Site site = new Site();
		Long id = (Long) jsonObjectImport.get("id");
		String siteName = (String) jsonObjectImport.get("site");
		String street = (String) jsonObjectImport.get("street");
		String postCode = (String) jsonObjectImport.get("post_code");
		String city = (String) jsonObjectImport.get("city");

		Address address = new Address();
		address.setStreet(street);
		address.setPostCode(postCode);
		address.setCity(city);

		site.setId(id);
		site.setName(siteName);
		site.setAddress(address);
		return site;
	}

	/**
	 * 
	 * Create JSONArray from import dans assign all DataPoints for each line
	 * 
	 */
	private List<DataPoint> collectConsos(JSONObject jsonObjectImport) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		JSONArray data = (JSONArray) jsonObjectImport.get("values");
		System.out.println("Nombre de valeurs " + data.size());

		for (Object obj : data) {
			JSONObject jsonObject = (JSONObject) obj;
			String dateString = (String) jsonObject.get("date");
			LocalDateTime date = LocalDateTime.parse(dateString, formatter);
			Double conversion = null;
			try {
				Double power = (Double) jsonObject.get("value");
				conversion = power;
			} catch (Exception ex) {
				Long longPower = (Long) jsonObject.get("value");
				conversion = longPower.doubleValue();
				System.out.println(dateString + " ; " + longPower + " -> " + conversion);
			} finally {
				DataPoint dataPoint = new DataPoint();
				dataPoint.setSite(getSite());
				dataPoint.setValue(conversion);
				dataPoint.setTime(date);
				getConsos().add(dataPoint);
			}
		}
		return consos;
	}

	public Site getSite() {
		return site;
	}

	public List<DataPoint> getConsos() {
		return consos;
	}

}
