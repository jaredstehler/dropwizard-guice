package com.fiestacabin.dropwizard.guice.test;

import com.fiestacabin.dropwizard.guice.AutoConfigService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.config.Environment;

public class SampleService extends AutoConfigService<SampleServiceConfiguration> {

	public SampleService() {
		super("sample-service", "com.fiestacabin.dropwizard.guice.test");
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
