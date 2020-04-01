package com.noebrito.openbrewerydb.models;

public enum FilterType {
	BY_CITY("by_city"),
	BY_NAME("by_name"),
	BY_STATE("by_state"),
	BY_POSTAL("by_postal"),
	BY_TYPE("by_type");

	public final String label;

	FilterType(String label) {
		this.label = label;
	}
}
