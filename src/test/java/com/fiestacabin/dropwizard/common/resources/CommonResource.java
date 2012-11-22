package com.fiestacabin.dropwizard.common.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.fiestacabin.dropwizard.guice.test.service.MyService;

@Path("common-resource")
public class CommonResource {

	private MyService myService;
	
	@Inject
	public CommonResource(MyService myService) {
		this.myService = myService;
	}
	
	@GET
	public Response doGet(){
		return Response.ok("We have so much in common!!!").build();
	}
	
	public MyService getMyService() {
		return myService;
	}
	
}

