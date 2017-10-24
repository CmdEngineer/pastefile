package com.cmdengineer.extra.utills;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HTTP {

	HttpClient client = new DefaultHttpClient();
	private final String USER_AGENT = "Mozilla/5.0";

	// HTTP GET request
	public String sendGet(String url) throws Exception {
		client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		// add request header
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);
		
		/*System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());*/
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		client.getConnectionManager().shutdown();
	    return result.toString();
	}

	// HTTP POST request
	public String sendPost(String url, List<NameValuePair> params) throws Exception {
		client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		post.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = client.execute(post);
		
		/*System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());*/

		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		client.getConnectionManager().shutdown();
		return result.toString();
	}
}
