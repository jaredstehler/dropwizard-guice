package com.fiestacabin.dropwizard.guice;

import io.dropwizard.Configuration;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Environment;

import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.api.InjectionResolver;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Injector;

/**
 * Application class which automatically adds items to the application environment, including
 * health checks, resources
 * 
 * @author jstehler
 * 
 */
public abstract class AutoConfigApplication<T extends Configuration> extends
		GuiceApplication<T> {
	private static final Logger LOG = LoggerFactory.getLogger(AutoConfigApplication.class);

	private Reflections reflections;

	protected AutoConfigApplication(String... basePackages) {
		ConfigurationBuilder cfgBldr = new ConfigurationBuilder();
		FilterBuilder filterBuilder = new FilterBuilder();
		
		if(basePackages.length == 0) {
		  basePackages = new String[]{getClass().getPackage().getName()};
		}
		
		for (String basePkg : basePackages) {
			cfgBldr.addUrls(ClasspathHelper.forPackage(basePkg));
			filterBuilder.include(FilterBuilder.prefix(basePkg));
		}

		cfgBldr.filterInputsBy(filterBuilder).setScanners(
				new SubTypesScanner(), new TypeAnnotationsScanner());
		this.reflections = new Reflections(cfgBldr);
	}

	@Override
	protected void runWithInjector(T configuration,
			Environment environment, Injector injector) throws Exception {

		addHealthChecks(environment, injector);
		addProviders(environment, injector);
		addInjectableProviders(environment, injector);
		addResources(environment, injector);
		addTasks(environment, injector);
		addManaged(environment, injector);
	}

	private void addManaged(Environment environment, Injector injector) {
		Set<Class<? extends Managed>> managedClasses = reflections
				.getSubTypesOf(Managed.class);
		for (Class<? extends Managed> managed : managedClasses) {
			environment.lifecycle().manage(injector.getInstance(managed));
			LOG.info("Added managed: " + managed);
		}
	}

	private void addTasks(Environment environment, Injector injector) {
		Set<Class<? extends Task>> taskClasses = reflections
				.getSubTypesOf(Task.class);
		for (Class<? extends Task> task : taskClasses) {
			environment.admin().addTask(injector.getInstance(task));
			LOG.info("Added task: " + task);
		}
	}

	private void addHealthChecks(Environment environment, Injector injector) {
		Set<Class<? extends HealthCheck>> healthCheckClasses = reflections
				.getSubTypesOf(HealthCheck.class);
		for (Class<? extends HealthCheck> healthCheck : healthCheckClasses) {
			environment.healthChecks().register(healthCheck.getSimpleName(), injector.getInstance(healthCheck));
			LOG.info("Added healthCheck: " + healthCheck);
		}
	}

	@SuppressWarnings("rawtypes")
	private void addInjectableProviders(Environment environment,
			Injector injector) {
	  Set<Class<? extends InjectionResolver>> injectionResolvers = reflections
	      .getSubTypesOf(InjectionResolver.class);
		for (Class<? extends InjectionResolver> injectableProvider : injectionResolvers) {
			environment.jersey().register(injector.getInstance(injectableProvider));
			LOG.info("Added injectableProvider: " + injectableProvider);
		}
	}

	private void addProviders(Environment environment, Injector injector) {
		Set<Class<?>> providerClasses = reflections
				.getTypesAnnotatedWith(Provider.class);
		for (Class<?> provider : providerClasses) {
			environment.jersey().register(injector.getInstance(provider));
			LOG.info("Added provider class: " + provider);
		}
	}

	private void addResources(Environment environment, Injector injector) {
		Set<Class<?>> resourceClasses = reflections
				.getTypesAnnotatedWith(Path.class);
		for (Class<?> resource : resourceClasses) {
			environment.jersey().register(injector.getInstance(resource));
			LOG.info("Added resource class: " + resource);
		}
	}

}
