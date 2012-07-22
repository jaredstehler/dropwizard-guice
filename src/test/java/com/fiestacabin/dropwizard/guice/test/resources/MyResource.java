package com.fiestacabin.dropwizard.guice.test.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.fiestacabin.dropwizard.guice.test.service.MyService;

@Path("my-resource")
public class MyResource {

	private MyService myService;
	
	@Inject
	public MyResource(MyService myService) {
		this.myService = myService;
	}
	
	@GET
	public Response doGet(){
		return Response.ok("hello world!").build();
	}
	
	public MyService getMyService() {
		return myService;
	}
	
}
