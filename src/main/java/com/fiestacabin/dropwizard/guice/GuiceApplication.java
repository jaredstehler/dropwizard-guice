package com.fiestacabin.dropwizard.guice;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class GuiceApplication<T extends Configuration> extends Application<T> {

	protected GuiceApplication() {}

	@Override
	public void initialize(Bootstrap<T> bootstrap) {
	}
	
	@Override
	public void run(T configuration, Environment environment) throws Exception {
		Injector injector = createInjector(configuration);
		injector.injectMembers(this);
		runWithInjector(configuration, environment, injector);
	}

	protected Injector createInjector(T configuration) {
		return Guice.createInjector();
	}
	
	protected abstract void runWithInjector(T configuration,
			Environment environment, Injector injector) throws Exception;
	
}
