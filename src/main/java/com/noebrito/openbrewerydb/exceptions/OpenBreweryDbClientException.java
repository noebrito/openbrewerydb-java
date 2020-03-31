package com.noebrito.openbrewerydb.exceptions;

public class OpenBreweryDbClientException extends Exception {

	public OpenBreweryDbClientException(String message) {
		super(message);
	}

	public OpenBreweryDbClientException(Throwable cause) {
		super(cause);
	}

	public OpenBreweryDbClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
