package com.noebrito.openbrewerydb;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noebrito.openbrewerydb.exceptions.OpenBreweryDbClientException;
import com.noebrito.openbrewerydb.models.Brewery;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Client wrapper for the Open Brewery DB Rest API.
 */
public class BreweryClient {

	private HttpClient httpClient;
	private String url;
	private Gson gson;

	/**
	 * Constructor
	 *
	 * @param url the endpoint url for the service.
	 */
	public BreweryClient(String url) {
		this.url = url;
		this.gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
		httpClient = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.build();
	}

	/**
	 * Returns the requested brewery.
	 *
	 * @param id the id of the brewery being requested.
	 * @return a {@link Brewery} object.
	 * @throws OpenBreweryDbClientException when a successful call is not made.
	 */
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
