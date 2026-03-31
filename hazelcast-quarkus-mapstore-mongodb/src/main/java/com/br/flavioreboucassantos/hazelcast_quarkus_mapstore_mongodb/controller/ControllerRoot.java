package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class ControllerRoot {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getRoot() {
		return "Olá";
	}
}
