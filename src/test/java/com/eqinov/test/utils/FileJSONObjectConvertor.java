package com.eqinov.test.utils;

import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * 
 * Class to convert json file into JSONObject
 *
 */
public class FileJSONObjectConvertor {

	private JSONObject jsonObjectImport = new JSONObject();

	public FileJSONObjectConvertor(String sourceDirectory, String fileName) {
		final String dir = System.getProperty("user.dir");
		File jsonFile = new File(dir + sourceDirectory + fileName);
		if (!jsonFile.exists()) {
			throw new IllegalStateException("Error : No file available from this path");
		}

		try {

			System.out.println("Original  path: " + jsonFile.getPath());
			JSONParser parser = new JSONParser();
			FileReader reader = new FileReader(jsonFile);

			this.jsonObjectImport = (JSONObject) parser.parse(reader);

		} catch (Exception e) {

			System.out.println("Exception in file conversion " + e);
		}
	}

	public JSONObject getJsonObjectImport() {
		return jsonObjectImport;
	}

}
