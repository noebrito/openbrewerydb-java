package com.noebrito.openbrewerydb.models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Class that describes how to filter the ListBreweries query.
 */
@Data
@Builder
public class ListBreweriesFilter {

	/**
	 * Describes the {@link FilterType}
	 */
	@NonNull
	private FilterType filterType;

	/**
	 * Value that is being filtered on. Spaces must be encoded with underscores or url encoding
	 * https://en.wikipedia.org/wiki/Percent-encoding
	 */
	@NonNull
	private String filterValue;
}
