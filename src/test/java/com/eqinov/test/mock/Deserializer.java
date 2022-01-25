package com.eqinov.test.mock;

import org.json.simple.JSONObject;

import com.eqinov.recrutement.data.Site;
import com.eqinov.test.utils.FileJSONObjectConvertor;
import com.eqinov.test.utils.JSONObjectDataConvertor;

/**
 * 
 * Main class to perform deserialization from json file
 *
 */
public class Deserializer {

	private static final String SOURCE_DIRECTORY = "/src/test/resources/__files/";
	private static final String FILE_NAME = "conso.json";

	public static void main(String[] args) {

		FileJSONObjectConvertor fileConvertor = new FileJSONObjectConvertor(SOURCE_DIRECTORY, FILE_NAME);
		JSONObject jsonObjectImport = fileConvertor.getJsonObjectImport();
		JSONObjectDataConvertor dataConvertor = new JSONObjectDataConvertor(jsonObjectImport);

		Site site = dataConvertor.getSite();
		System.out.println("Site " + site);

	}

}
