package com.br.flavioreboucassantos.hazelcast_client_quarkus.controller;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.dto.DTOPersonFact;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityPersonFact;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fact")
public class ControllerEntityPersonFact {

	private final Logger LOG = LoggerFactory.getLogger(ControllerEntityPersonFact.class);

	final HazelcastInstance hazelcastInstance;
	final IMap<Long, Object> mapPersonProfile;

	@Inject
	public ControllerEntityPersonFact(final HazelcastInstance hazelcastInstance,
			@ConfigProperty(name = "mapName.PersonFact") final String mapName) {
		this.hazelcastInstance = hazelcastInstance;
		mapPersonProfile = hazelcastInstance.getMap(mapName);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(final DTOPersonFact dtoPersonFact) {
		if (dtoPersonFact != null
				&& dtoPersonFact.id() > 0
				&& dtoPersonFact.idPersonProfile() > 0) {

			final EntityPersonFact entityPersonFact = new EntityPersonFact(dtoPersonFact, System.currentTimeMillis());

			mapPersonProfile.put(dtoPersonFact.id(), entityPersonFact);

			return Response.ok(entityPersonFact).status(Response.Status.ACCEPTED).build();

		} else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
	}

}
