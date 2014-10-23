package com.fiestacabin.dropwizard.guice;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Environment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fiestacabin.dropwizard.common.resources.CommonResource;
import com.fiestacabin.dropwizard.guice.test.MultiPackageApplication;
import com.fiestacabin.dropwizard.guice.test.SampleApplication;
import com.fiestacabin.dropwizard.guice.test.SampleServiceConfiguration;
import com.fiestacabin.dropwizard.guice.test.health.MyHealthCheck;
import com.fiestacabin.dropwizard.guice.test.resources.MyResource;
import com.fiestacabin.dropwizard.guice.test.tasks.MyTask;


public class AutoConfigApplicationTest {

	@Mock private SampleServiceConfiguration configuration;
	@Mock private Environment environment;
	@Mock private AdminEnvironment adminEnvironment;
	@Mock private HealthCheckRegistry healthCheckRegistry;
	@Mock private JerseyEnvironment jerseyEnvironment;
  @Mock private LifecycleEnvironment lifecycleEnvironment;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		when(environment.admin()).thenReturn(adminEnvironment);
		when(environment.healthChecks()).thenReturn(healthCheckRegistry);
		when(environment.jersey()).thenReturn(jerseyEnvironment);
		when(environment.lifecycle()).thenReturn(lifecycleEnvironment);
	}
	
	@Test
	public void itInstallsResources() throws Exception {
		SampleApplication s = new SampleApplication();
		s.run(configuration, environment);
		
		ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
		verify(jerseyEnvironment).register(resource.capture());
		assertThat(resource.getValue(), is(MyResource.class));
	}
	
	@Test
	public void itInstallsMultiPackageResources() throws Exception {
		MultiPackageApplication s = new MultiPackageApplication();
		s.run(configuration, environment);
		
		ArgumentCaptor<?> captor = ArgumentCaptor.forClass(Object.class);
		verify(jerseyEnvironment, times(2)).register(captor.capture());
		
		List<?> values = captor.getAllValues();
		assertEquals(2, values.size());
		
		Set<Class<?>> expectedResults = new HashSet<Class<?>>();
	  expectedResults.add(MyResource.class);
	  expectedResults.add(CommonResource.class);
	  for(Object obj : values){
	    Class<?> cls = obj.getClass();
	    expectedResults.remove(cls);
	  }
	  
	  assertTrue(expectedResults.isEmpty());
	}
	
	@Test
	public void itWiresUpDependencies() throws Exception {
		SampleApplication s = new SampleApplication();
		s.run(configuration, environment);
		
		ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
		verify(jerseyEnvironment).register(resource.capture());
		
		MyResource r = resource.getValue();
		assertThat(r.getMyService(), not(nullValue()));
		assertThat(r.getMyService().getMyOtherService(), not(nullValue()));
	}
	
	@Test
	public void itInstallsHealthChecks() throws Exception {
		SampleApplication s = new SampleApplication();
		s.run(configuration, environment);

		ArgumentCaptor<? extends HealthCheck> healthCheck = ArgumentCaptor.forClass(HealthCheck.class);
		verify(healthCheckRegistry).register(eq("MyHealthCheck"), healthCheck.capture());
		assertThat(healthCheck.getValue(), is(MyHealthCheck.class));
	}
	
	@Test
	public void itInstallsTasks() throws Exception {
		SampleApplication s = new SampleApplication();
		s.run(configuration, environment);
		
		ArgumentCaptor<? extends Task> task = ArgumentCaptor.forClass(Task.class);
		verify(adminEnvironment).addTask(task.capture());
		assertThat(task.getValue(), is(MyTask.class));
	}
}
