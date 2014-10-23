package com.fiestacabin.dropwizard.guice.test;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SampleServiceConfiguration extends Configuration {

	@JsonProperty
	private String foo;
	
	public String getFoo() {
		return foo;
	}
	
}
