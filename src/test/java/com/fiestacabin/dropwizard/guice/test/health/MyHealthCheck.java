package com.fiestacabin.dropwizard.guice.test.health;

import com.yammer.metrics.core.HealthCheck;

public class MyHealthCheck extends HealthCheck {

	public MyHealthCheck() {
		super("my-health");
	}
	
	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}
