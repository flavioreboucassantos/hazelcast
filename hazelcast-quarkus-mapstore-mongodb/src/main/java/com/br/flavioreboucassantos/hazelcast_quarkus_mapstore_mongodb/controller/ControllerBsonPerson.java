package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOBsonPerson;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/person")
public class ControllerBsonPerson {

	private final Logger LOG = LoggerFactory.getLogger(ControllerBsonPerson.class);

	final HazelcastInstance hazelcastInstance;
	final IMap<String, BsonPerson> map;

	@Inject
	public ControllerBsonPerson(final HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
		map = hazelcastInstance.getMap("bsonPerson");
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(final DTOBsonPerson dtoBsonPerson) {
		if (dtoBsonPerson != null && !dtoBsonPerson.id().isBlank() && !dtoBsonPerson.id().isEmpty()) {
			map.put(dtoBsonPerson.id(), new BsonPerson(dtoBsonPerson));
			return Response.ok(dtoBsonPerson).status(Response.Status.ACCEPTED).build();
		} else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<BsonPerson> getAll() {
		Predicate<String, BsonPerson> predicate = Predicates.and(
				Predicates.greaterEqual("age", 18),
				Predicates.lessEqual("age", 65));
		Collection<BsonPerson> result = map.values(predicate);
		return result;
	}

}
