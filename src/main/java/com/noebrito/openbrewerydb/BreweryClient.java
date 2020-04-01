package com.noebrito.openbrewerydb;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.noebrito.openbrewerydb.exceptions.OpenBreweryDbClientException;
import com.noebrito.openbrewerydb.models.Brewery;
import com.noebrito.openbrewerydb.models.FilterType;
import com.noebrito.openbrewerydb.models.ListBreweriesFilter;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

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
					.GET()
					.uri(URI.create(getBreweryUrl))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			validateResponse(response);
			brewery = gson.fromJson(response.body(), Brewery.class);
		} catch (Exception e) {
			throw new OpenBreweryDbClientException(e);
		}

		return brewery;
	}

	/**
	 * Returns a list of breweries based on the provided filter.
	 *
	 * @param filter {@link ListBreweriesFilter} for filtering breweries.
	 * @return a list of Brewery objects.
	 * @throws OpenBreweryDbClientException if service call fails.
	 */
	public List<Brewery> listBreweries(ListBreweriesFilter filter) throws OpenBreweryDbClientException {
		FilterType filterType = filter.getFilterType();
		String filterValue = filter.getFilterValue();
		String listBreweriesUrl = String.format("%s/breweries?%s=%s", url, filterType.label, filterValue);
		return executeGetRequest(listBreweriesUrl);
	}

	public List<Brewery> searchBreweries(String query) throws OpenBreweryDbClientException {
		String searchBreweriesUrl = String.format("%s/breweries/search?query=%s", url, query);
		return executeGetRequest(searchBreweriesUrl);
	}

	private List<Brewery> executeGetRequest(String url) throws OpenBreweryDbClientException {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.GET()
					.uri(URI.create(url))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			validateResponse(response);
			Type listType = new TypeToken<List<Brewery>>(){}.getType();
			return gson.fromJson(response.body(), listType);
		} catch (Exception e) {
			throw new OpenBreweryDbClientException(e);
		}
	}

	private void validateResponse(HttpResponse<String> response) throws OpenBreweryDbClientException {
		if (response.statusCode() != 200) {
			throw new OpenBreweryDbClientException("Received a non 200 status code.");
		}
	}
}
