package com.fiestacabin.dropwizard.guice;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiestacabin.dropwizard.guice.test.SampleService;
import com.fiestacabin.dropwizard.guice.test.SampleServiceConfiguration;
import com.fiestacabin.dropwizard.guice.test.health.MyHealthCheck;
import com.fiestacabin.dropwizard.guice.test.resources.MyResource;
import com.fiestacabin.dropwizard.guice.test.tasks.MyTask;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;


public class AutoConfigServiceTest {

	@Mock private SampleServiceConfiguration configuration;
	@Mock private Environment environment;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void itInstallsResources() throws Exception {
		SampleService s = new SampleService();
		s.initializeWithBundles(configuration, environment);
		
		ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
		verify(environment).addResource(resource.capture());
		assertThat(resource.getValue(), is(MyResource.class));
	}
	
	@Test
	public void itWiresUpDependencies() throws Exception {
		SampleService s = new SampleService();
		s.initializeWithBundles(configuration, environment);
		
		ArgumentCaptor<MyResource> resource = ArgumentCaptor.forClass(MyResource.class);
		verify(environment).addResource(resource.capture());
		
		MyResource r = resource.getValue();
		assertThat(r.getMyService(), not(nullValue()));
		assertThat(r.getMyService().getMyOtherService(), not(nullValue()));
	}
	
	@Test
	public void itInstallsHealthChecks() throws Exception {
		SampleService s = new SampleService();
		s.initializeWithBundles(configuration, environment);

		ArgumentCaptor<? extends HealthCheck> healthCheck = ArgumentCaptor.forClass(HealthCheck.class);
		verify(environment).addHealthCheck(healthCheck.capture());
		assertThat(healthCheck.getValue(), is(MyHealthCheck.class));
	}
	
	@Test
	public void itInstallsTasks() throws Exception {
		SampleService s = new SampleService();
		s.initializeWithBundles(configuration, environment);
		
		ArgumentCaptor<? extends Task> task = ArgumentCaptor.forClass(Task.class);
		verify(environment).addTask(task.capture());
		assertThat(task.getValue(), is(MyTask.class));
	}
}
