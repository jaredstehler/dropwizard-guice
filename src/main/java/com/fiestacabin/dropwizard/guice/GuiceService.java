package com.fiestacabin.dropwizard.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;

public abstract class GuiceService<T extends Configuration> extends Service<T> {

	protected GuiceService(String name) {
		super(name);
	}

	protected void initialize(T configuration, Environment environment) throws Exception {
		Injector injector = createInjector(configuration);
		injector.injectMembers(this);
		initializeWithInjector(configuration, environment, injector);
	}

	protected Injector createInjector(T configuration) {
		return Guice.createInjector();
	}
	
	protected abstract void initializeWithInjector(T configuration,
			Environment environment, Injector injector) throws Exception;
	
}
