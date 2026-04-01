package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity.BsonPersonProfile;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.comparator.ComparatorBsonPersonProfile;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOBsonPersonProfile;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.PagingPredicate;
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
public class ControllerBsonPersonProfile {

	private final Logger LOG = LoggerFactory.getLogger(ControllerBsonPersonProfile.class);

	final HazelcastInstance hazelcastInstance;
	final IMap<Long, BsonPersonProfile> map;

	@Inject
	public ControllerBsonPersonProfile(final HazelcastInstance hazelcastInstance) {
		this.hazelcastInstance = hazelcastInstance;
		map = hazelcastInstance.getMap("bsonPersonProfile");
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(final DTOBsonPersonProfile dtoBsonPersonProfile) {
		if (dtoBsonPersonProfile != null && dtoBsonPersonProfile.id() > 0) {
			final BsonPersonProfile bsonPersonProfile = new BsonPersonProfile(dtoBsonPersonProfile, System.currentTimeMillis());

			map.put(dtoBsonPersonProfile.id(), bsonPersonProfile);

			return Response.ok(bsonPersonProfile).status(Response.Status.ACCEPTED).build();

		} else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<BsonPersonProfile> getAll() {
		final Predicate<Long, BsonPersonProfile> filteringPredicate = Predicates.and(
				Predicates.greaterEqual("age", 18),
				Predicates.lessEqual("age", 65));

		/*
		 * Get the last two using tsCreated.
		 * Selects only candidates between 18 and 65 years old.
		 */
		final PagingPredicate<Long, BsonPersonProfile> pagingPredicate = Predicates.pagingPredicate(filteringPredicate, new ComparatorBsonPersonProfile(), 2);

		Collection<BsonPersonProfile> result = map.values(pagingPredicate);

		return result;
	}

}
