package com.lymbix;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.lymbix.models.ArticleInfo;

public class LymbixClient {

    private final String API_BASE = "http://gyrus.lymbix.com/";
    private final String TONALIZE_MULTIPLE = "tonalize_multiple";
    private final String TONALIZE_DETAILED = "tonalize_detailed";
    private final String TONALIZE = "tonalize";
    private final String FLAG_RESPONSE = "flag_response";
	
	private Gson json = new Gson();
	private String authenticationKey = null;
	
	/*
	 * @param authenticationKey your Lymbix authentication key
	 */
	public LymbixClient(String authenticationKey) throws Exception {
		if (authenticationKey == null || authenticationKey == "") {
			throw new Exception("You must include your authentication key");
		}
		
		this.authenticationKey = authenticationKey;
	}
	
	/* utility functions */
	
	private String post(String endpoint, String data, ArrayList<String> headers) throws Exception {
	    URL url;
		HttpURLConnection connection = null;  
	    try {
	      url = new URL(endpoint);
	      connection = (HttpURLConnection)url.openConnection();
	      
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	      connection.setRequestProperty("Accept", "application/json");
	      connection.setRequestProperty("Content-Length", Integer.toString(data.getBytes().length));  
	      
	      for (String header : headers) {
	    	  String[] headerComponents = header.split(":");
	    	  connection.setRequestProperty(headerComponents[0], headerComponents[1]);
	      }
	      
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      // write request
	      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
	      wr.writeBytes (data);
	      wr.flush ();
	      wr.close ();

	      // read response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      
	      return response.toString();
	      
	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}
	
	private ArrayList<String> getHeaders() {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("AUTHENTICATION: " + authenticationKey);
		headers.add("VERSION: 2.2");
		return headers;
	}
	
	/* api methods */
	
	/*
	 * tonalize multiple articles
	 * 
	 * @param articles articles to tonalize
	 * 
	 * @param options additional parameters (reference_ids and return_fields)
	 * 
	 * @return see the api documentation for the format of this object
	 */
	public ArticleInfo[] tonalizeMultiple(String[] articles, Map<String, String> options) throws Exception {
		if (articles == null) {
			throw new Exception("You must include articles to tonalize");
		}
		
		String url = API_BASE + TONALIZE_MULTIPLE;
		String data = "articles=" + URLEncoder.encode(json.toJson(articles), "UTF-8");
		
		if (options != null) {
			for (Map.Entry<String, String> entry : options.entrySet()) {
				data += "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
			}
		}
		
		ArrayList<String> headers = getHeaders();
		String response = post(url, data, headers);
		return json.fromJson(response, ArticleInfo[].class);
	}
	
	/*
	 * tonalize an article
	 * 
	 * @param article article to tonalize
	 * 
	 * @param options additional parameters (reference_id and return_fields)
	 * 
	 * @return see the api documentation for the format of this object
	 */
	public ArticleInfo tonalizeDetailed(String article, Map<String, String> options) throws Exception {
		if (article == null) {
			throw new Exception("You must include an article to tonalize");
		}
		
		String url = API_BASE + TONALIZE_DETAILED;
		String data = "article=" + URLEncoder.encode(article, "UTF-8");
		
		if (options != null) {
			for (Map.Entry<String, String> entry : options.entrySet()) {
				data += "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
			}
		}
		
		ArrayList<String> headers = getHeaders();
		String response = post(url, data, headers);
		return json.fromJson(response, ArticleInfo.class);
	}
	
	/*
	 * tonalize an article
	 * 
	 * @param article article to tonalize
	 * 
	 * @param options additional parameters (reference_id and return_fields)
	 * 
	 * @return see the api documentation for the format of this object
	 */
	public ArticleInfo tonalize(String article, Map<String, String> options) throws Exception {
		if (article == null) {
			throw new Exception("You must include an article to tonalize");
		}
		
		String url = API_BASE + TONALIZE;
		String data = "article=" + URLEncoder.encode(article, "UTF-8");
		
		if (options != null) {
			for (Map.Entry<String, String> entry : options.entrySet()) {
				data += "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
			}
		}
		
		ArrayList<String> headers = getHeaders();
		String response = post(url, data, headers);
		return json.fromJson(response, ArticleInfo.class);
	}
	
	/*
	 * flag a response as inaccurate
	 * 
	 * @param phrase the phrase that returns an inaccurate response
	 * 
	 * @param the method that returns an inaccurate response
	 * 
	 * @param apiVersion the version that returns an inaccurate response
	 * 
	 * @param callbackUrl a url to call when the phrase has been re-rated
	 * 
	 * @param options additional parameters (reference_id)
	 * 
	 * @return see the api documentation for this method's response
	 */
	public String FlagResponse(String phrase, String apiMethod, String apiVersion, String callbackUrl, Map<String, String> options) throws Exception {
		if (phrase == null) {
			throw new Exception("You must include a phrase to flag");
		}
		
		String url = API_BASE + FLAG_RESPONSE;
		String data = "phrase=" + URLEncoder.encode(phrase, "UTF-8");
		
		if (apiVersion == null) apiVersion = "2.2";
		if (apiMethod != null) data += "&apiMethod=" + URLEncoder.encode(apiMethod, "UTF-8");
		if (apiVersion != null) data += "&apiVersion=" + URLEncoder.encode(apiVersion, "UTF-8");
		if (callbackUrl != null) data += "&callbackUrl=" + URLEncoder.encode(callbackUrl, "UTF-8");
		
		if (options != null) {
			for (Map.Entry<String, String> entry : options.entrySet()) {
				data += "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
			}
		}
		
		ArrayList<String> headers = getHeaders();
		String response = post(url, data, headers);
		return response;
	}
}
