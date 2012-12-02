package com.fiestacabin.dropwizard.guice.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class SampleServiceConfiguration extends Configuration {

	@JsonProperty
	private String foo;
	
	public String getFoo() {
		return foo;
	}
	
}
