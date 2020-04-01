package com.noebrito.openbrewerydb;

import com.noebrito.openbrewerydb.exceptions.OpenBreweryDbClientException;
import com.noebrito.openbrewerydb.models.Brewery;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;


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
			breweryClient.getBrewery(1); })
				.isInstanceOf(OpenBreweryDbClientException.class);
	}
}