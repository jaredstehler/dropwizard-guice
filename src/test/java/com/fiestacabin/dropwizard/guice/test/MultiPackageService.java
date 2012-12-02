package com.fiestacabin.dropwizard.guice.test;

import com.fiestacabin.dropwizard.guice.AutoConfigService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.config.Environment;

/**
 * A test service that uses the varargs constructor for AutoConfigService
 * @author ggavares
 *
 */
public class MultiPackageService extends AutoConfigService<SampleServiceConfiguration> {
  
  public MultiPackageService() {
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
