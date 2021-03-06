package com.noebrito.openbrewerydb;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.noebrito.openbrewerydb.exceptions.OpenBreweryDbClientException;
import com.noebrito.openbrewerydb.models.Brewery;
import com.noebrito.openbrewerydb.models.FilterType;
import com.noebrito.openbrewerydb.models.ListBreweriesFilter;
import lombok.NonNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Client wrapper for the Open Brewery DB Rest API.
 */
public class BreweryClient {

	private final HttpClient httpClient;
	private final String url;
	private final Gson gson;

	/**
	 * Returns a new {@link BreweryClient}.
	 *
	 * @param url apiUrl for the open brewery db service.
	 * @return a BreweryClient instance.
	 */
	public static BreweryClient createClient(String url) {
		return new BreweryClient(url);
	}

	/**
	 * Private constructor.
	 *
	 * @param url endpoint url for the service.
	 */
	private BreweryClient(String url) {
		checkNotNull(url);
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
		} catch (IllegalArgumentException | IOException | InterruptedException e) {
			throw new OpenBreweryDbClientException("Error getting brewery from the database.", e);
		}

		return brewery;
	}

	/**
	 * Returns a list of breweries based on the provided filter.
	 *
	 * @param filter {@link ListBreweriesFilter} for filtering breweries.
	 * @return a list of Brewery objects.
	 * @throws OpenBreweryDbClientException if the service call fails.
	 */
	public List<Brewery> listBreweries(ListBreweriesFilter filter) throws OpenBreweryDbClientException {
		checkNotNull(filter);
		FilterType filterType = filter.getFilterType();
		String filterValue = filter.getFilterValue();
		String listBreweriesUrl = String.format("%s/breweries?%s=%s", url, filterType.label, filterValue);
		return executeListBreweriesRequest(listBreweriesUrl);
	}

	/**
	 * Returns a list of breweries based on the search term provided in the query.
	 *
	 * @param query Search term being queried.
	 * @return a list of Brewery objects.
	 * @throws OpenBreweryDbClientException if the service call fails.
	 */
	public List<Brewery> searchBreweries(String query) throws OpenBreweryDbClientException {
		checkNotNull(query);
		String searchBreweriesUrl = String.format("%s/breweries/search?query=%s", url, query);
		return executeListBreweriesRequest(searchBreweriesUrl);
	}

	private List<Brewery> executeListBreweriesRequest(String url) throws OpenBreweryDbClientException {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.GET()
					.uri(URI.create(url))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			validateResponse(response);
			Type listType = new TypeToken<List<Brewery>>(){}.getType();
			return gson.fromJson(response.body(), listType);
		} catch (IllegalArgumentException | IOException | InterruptedException e) {
			throw new OpenBreweryDbClientException("Error searching breweries from the Open Brewery DB Service.", e);
		}
	}

	private void validateResponse(HttpResponse<String> response) throws OpenBreweryDbClientException {
		if (response.statusCode() != 200) {
			throw new OpenBreweryDbClientException(
					String.format("Failed call to the Open Brewery DB Service. Error body: %s", response.body())
			);
		}
	}
}
