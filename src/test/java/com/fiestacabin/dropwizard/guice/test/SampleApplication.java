package com.fiestacabin.dropwizard.guice.test;

import io.dropwizard.setup.Environment;

import com.fiestacabin.dropwizard.guice.AutoConfigApplication;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class SampleApplication extends AutoConfigApplication<SampleServiceConfiguration> {

	public SampleApplication() {
		super("com.fiestacabin.dropwizard.guice.test");
	}
	
	@Override
	protected Injector createInjector(SampleServiceConfiguration configuration) {
		return Guice.createInjector(new SampleServiceModule());
	}
	
	@Override
	protected void runWithInjector(
			SampleServiceConfiguration configuration, Environment environment,
			Injector injector) throws Exception {
		super.runWithInjector(configuration, environment, injector);
	}
	
}
