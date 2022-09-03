package br.com.samsung.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EvaluationAPI {
	
	final static String CURRENCY = "https://cellolatam.cellologistics.com.br/sds-devs-evaluation/evaluation/currency";
	final static String QUOTATION = "https://cellolatam.cellologistics.com.br/sds-devs-evaluation/evaluation/quotation";
	final static String DOCS = "https://cellolatam.cellologistics.com.br/sds-devs-evaluation/evaluation/docs";

	public String searchURLAPI (String api) {
	
		try {
	
			String url = null;
			
			switch(api) {
				case "CURRENCY":
					url = CURRENCY;
					break;
				case "QUOTATION":
				  url = QUOTATION;
				  break;
				case "DOCS":
				  url = DOCS;
				  break;
				default:
			    // code block
			}
			
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            String line;
            while ((line = br.readLine()) != null) {
                output += line;
            }

            conn.disconnect();
            
            return output;
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}