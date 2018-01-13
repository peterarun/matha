package com.matha.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class DataUtils {

	String NEW_LINE = System.getProperty("line.separator");

	@Test
	public void convertCsvToJson() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(
				new FileReader("E:\\Matha\\Billing\\matha\\dbScripts\\ExportedData\\Books.psv"));
				BufferedWriter bw = new BufferedWriter(
						new FileWriter("E:\\Matha\\Billing\\matha\\dbScripts\\ExportedData\\Books.json"));) {

			// Header
			String hLine = br.readLine();
			String[] colHeads = hLine.split("\\|");

			String cLine;
			while ((cLine = br.readLine()) != null) {
				String[] colData = cLine.split("\\|");
				StringBuilder strBld = new StringBuilder();
				strBld.append("{");
				int i = 0;
				for (; i < colHeads.length - 1; i++) {
					addColData(strBld, colHeads[i], colData[i]);
					strBld.append(",");
				}
				addColData(strBld, colHeads[i], colData[i]);
				strBld.append("}");
				System.out.println(strBld);
				strBld.append(NEW_LINE);				
				bw.write(strBld.toString());
			}

		}
	}

	public StringBuilder addColData(StringBuilder strBld, String header, String data) {		
		strBld.append("\"");
		strBld.append(header);
		strBld.append("\"");
		strBld.append(": ");
		if (data == null) {
			strBld.append("\"\"");
		} else if (data.startsWith("{")) {
			strBld.append(data);
		} else {
			strBld.append("\"");
			strBld.append(data);
			strBld.append("\"");
		}		
		return strBld;
	}
}
