package com.noebrito.openbrewerydb;

import com.noebrito.openbrewerydb.exceptions.OpenBreweryDbClientException;
import com.noebrito.openbrewerydb.models.Brewery;
import com.noebrito.openbrewerydb.models.FilterType;
import com.noebrito.openbrewerydb.models.ListBreweriesFilter;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;


class ClientTest implements WithAssertions {

	public static final String ENDPOINT = "https://api.openbrewerydb.org";

	@Test
	void testGetBrewery() throws OpenBreweryDbClientException {
		BreweryClient breweryClient = new BreweryClient(ENDPOINT);
		Brewery brewery = breweryClient.getBrewery(1);

		assertThat(brewery).isNotNull();
		assertThat(brewery.getId()).isEqualTo(1);
	}

	@Test
	void testGetBreweryThrowsException() {
		BreweryClient breweryClient = new BreweryClient("bad");
		assertThatThrownBy(() -> {
			breweryClient.getBrewery(1);
		})
				.isInstanceOf(OpenBreweryDbClientException.class);
	}

	@Test
	void testListBreweries() throws OpenBreweryDbClientException {
		BreweryClient breweryClient = new BreweryClient(ENDPOINT);
		ListBreweriesFilter filter = ListBreweriesFilter.builder()
				.filterType(FilterType.BY_CITY)
				.filterValue("los_angeles")
				.build();

		List<Brewery> breweries = breweryClient.listBreweries(filter);

		assertThat(breweries).isNotNull();
		assertThat(breweries.size()).isGreaterThan(0);
	}

	@Test
	void testSearchBreweries() throws OpenBreweryDbClientException {
		BreweryClient breweryClient = new BreweryClient(ENDPOINT);

		List<Brewery> breweries = breweryClient.searchBreweries("dog");

		assertThat(breweries).isNotNull();
		assertThat(breweries.size()).isGreaterThan(0);
	}
}
