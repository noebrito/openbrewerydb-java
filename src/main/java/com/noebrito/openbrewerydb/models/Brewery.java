package com.noebrito.openbrewerydb.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Brewery model.
 */
@Data
@Builder(toBuilder = true)
public class Brewery {

	private int id;
	private String name;
	private String breweryType;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String longitude;
	private String latitude;
	private String phone;
	private String websiteUrl;
	private Date updatedAt;
	private List<String> tagList;
}
