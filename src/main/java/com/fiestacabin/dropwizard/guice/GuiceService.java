package com.fiestacabin.dropwizard.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;

public abstract class GuiceService<T extends Configuration> extends Service<T> {

	private String name;
	
	protected GuiceService() {}
	
	protected GuiceService(String name) {
		this.name = name;
	}

	@Override
	public void initialize(Bootstrap<T> bootstrap) {
		if( name != null )
			bootstrap.setName(name);
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
