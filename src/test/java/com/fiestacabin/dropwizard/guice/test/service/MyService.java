package com.fiestacabin.dropwizard.guice.test.service;

import javax.inject.Inject;

public class MyService {

	private MyOtherService myOtherService;
	
	@Inject
	public MyService(MyOtherService myOtherService) {
		this.myOtherService = myOtherService;
	}
	
	public MyOtherService getMyOtherService() {
		return myOtherService;
	}
	
}
