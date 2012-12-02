package com.fiestacabin.dropwizard.guice;

import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import com.yammer.dropwizard.lifecycle.Managed;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;

/**
 * Service which automatically adds items to the service environment, including
 * health checks, resources
 * 
 * @author jstehler
 * 
 */
public abstract class AutoConfigService<T extends Configuration> extends
		GuiceService<T> {
	private static final Logger LOG = LoggerFactory.getLogger(AutoConfigService.class);

	private Reflections reflections;

	protected AutoConfigService(String name, String... basePackages) {
		super(name);

		ConfigurationBuilder cfgBldr = new ConfigurationBuilder();
		FilterBuilder filterBuilder = new FilterBuilder();
		for (String basePkg : basePackages) {
			cfgBldr.addUrls(ClasspathHelper.forPackage(basePkg));
			filterBuilder.include(FilterBuilder.prefix(basePkg));
		}

		cfgBldr.filterInputsBy(filterBuilder).setScanners(
				new SubTypesScanner(), new TypeAnnotationsScanner());
		this.reflections = new Reflections(cfgBldr);
	}

	protected AutoConfigService(String basePackage) {
		this(null, basePackage);
	}

	protected AutoConfigService() {
		super(null);
		this.reflections = new Reflections(getClass().getPackage().getName(),
				new SubTypesScanner(), new TypeAnnotationsScanner());
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
			environment.manage(injector.getInstance(managed));
			LOG.info("Added managed: " + managed);
		}
	}

	private void addTasks(Environment environment, Injector injector) {
		Set<Class<? extends Task>> taskClasses = reflections
				.getSubTypesOf(Task.class);
		for (Class<? extends Task> task : taskClasses) {
			environment.addTask(injector.getInstance(task));
			LOG.info("Added task: " + task);
		}
	}

	private void addHealthChecks(Environment environment, Injector injector) {
		Set<Class<? extends HealthCheck>> healthCheckClasses = reflections
				.getSubTypesOf(HealthCheck.class);
		for (Class<? extends HealthCheck> healthCheck : healthCheckClasses) {
			environment.addHealthCheck(injector.getInstance(healthCheck));
			LOG.info("Added healthCheck: " + healthCheck);
		}
	}

	@SuppressWarnings("rawtypes")
	private void addInjectableProviders(Environment environment,
			Injector injector) {
		Set<Class<? extends InjectableProvider>> injectableProviders = reflections
				.getSubTypesOf(InjectableProvider.class);
		for (Class<? extends InjectableProvider> injectableProvider : injectableProviders) {
			environment.addProvider(injector.getInstance(injectableProvider));
			LOG.info("Added injectableProvider: " + injectableProvider);
		}
	}

	private void addProviders(Environment environment, Injector injector) {
		Set<Class<?>> providerClasses = reflections
				.getTypesAnnotatedWith(Provider.class);
		for (Class<?> provider : providerClasses) {
			environment.addProvider(injector.getInstance(provider));
			LOG.info("Added provider class: " + provider);
		}
	}

	private void addResources(Environment environment, Injector injector) {
		Set<Class<?>> resourceClasses = reflections
				.getTypesAnnotatedWith(Path.class);
		for (Class<?> resource : resourceClasses) {
			environment.addResource(injector.getInstance(resource));
			LOG.info("Added resource class: " + resource);
		}
	}

}
