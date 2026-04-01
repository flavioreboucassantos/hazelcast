package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity.BsonPersonFact;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOPersonFact;
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
public class ControllerBsonPersonFact {

	private final Logger LOG = LoggerFactory.getLogger(ControllerBsonPersonFact.class);

	final HazelcastInstance hazelcastInstance;
	final IMap<Long, Object> map;

	@Inject
	public ControllerBsonPersonFact(final HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
		map = hazelcastInstance.getMap("bsonPersonFact");
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(final DTOPersonFact dtoPersonFact) {
		if (dtoPersonFact != null
				&& dtoPersonFact.id() > 0
				&& dtoPersonFact.idPersonProfile() > 0) {

			final BsonPersonFact bsonPersonFact = new BsonPersonFact(dtoPersonFact, System.currentTimeMillis());

			map.put(dtoPersonFact.id(), bsonPersonFact);

			return Response.ok(bsonPersonFact).status(Response.Status.ACCEPTED).build();

		} else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
	}

}
