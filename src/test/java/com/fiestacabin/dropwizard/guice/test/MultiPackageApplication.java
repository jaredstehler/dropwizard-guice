package com.fiestacabin.dropwizard.guice.test;

import io.dropwizard.setup.Environment;

import com.fiestacabin.dropwizard.guice.AutoConfigApplication;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * A test service that uses the varargs constructor for AutoConfigService
 * @author ggavares
 *
 */
public class MultiPackageApplication extends AutoConfigApplication<SampleServiceConfiguration> {
  
  public MultiPackageApplication() {
    super("sample-service", "com.fiestacabin.dropwizard.guice.test", "com.fiestacabin.dropwizard.common.resources");
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
