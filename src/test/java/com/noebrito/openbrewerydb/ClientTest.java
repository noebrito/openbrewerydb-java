package com.noebrito.openbrewerydb;

import com.noebrito.openbrewerydb.exceptions.OpenBreweryDbClientException;
import com.noebrito.openbrewerydb.models.Brewery;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;


class ClientTest implements WithAssertions {

	public static final String ENDPOINT = "https://api.openbrewerydb.org";

	@Test
	void testGetBrewery() throws OpenBreweryDbClientException {
		Client client = new Client(ENDPOINT);
		Brewery brewery = client.getBrewery(1);
		assertThat(brewery.getId()).isEqualTo(1);
	}

	@Test
	void testGetBreweryThrowsException() {
		Client client = new Client("bad");
		assertThatThrownBy(() -> {client.getBrewery(1); })
				.isInstanceOf(OpenBreweryDbClientException.class);
	}

}