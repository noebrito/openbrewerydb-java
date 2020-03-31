package com.noebrito.openbrewerydb;

import com.google.gson.Gson;
import com.noebrito.openbrewerydb.exceptions.OpenBreweryDbClientException;
import com.noebrito.openbrewerydb.models.Brewery;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {

	private HttpClient httpClient;
	private String url;
	private Gson gson;

	public Client(String url) {
		this.url = url;
		this.gson = new Gson();
		httpClient = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.build();
	}


	public Brewery getBrewery(int id) throws OpenBreweryDbClientException {
		String getBreweryUrl = String.format("%s/breweries/%s", url, id);
		Brewery brewery;
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(getBreweryUrl))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				throw new OpenBreweryDbClientException("Bad Request");
			}
			brewery = gson.fromJson(response.body(), Brewery.class);
		} catch (Exception e) {
			throw new OpenBreweryDbClientException(e);
		}

		return brewery;
	}



}
