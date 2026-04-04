package com.br.flavioreboucassantos.hazelcast_client_quarkus.controller;

import java.util.Collection;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator.ComparatorPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.dto.DTOPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityPersonProfile;
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
public class ControllerEntityPersonProfile {

	private final Logger LOG = LoggerFactory.getLogger(ControllerEntityPersonProfile.class);

	final HazelcastInstance hazelcastInstance;
	final IMap<Long, EntityPersonProfile> mapPersonProfile;

	@Inject
	public ControllerEntityPersonProfile(
			final HazelcastInstance hazelcastInstance,
			@ConfigProperty(name = "myApp.hazelcast.PersonProfile.mapName") final String mapName) {
		this.hazelcastInstance = hazelcastInstance;
		mapPersonProfile = hazelcastInstance.getMap(mapName);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(final DTOPersonProfile dtoPersonProfile) {
		if (dtoPersonProfile != null && dtoPersonProfile.id() > 0) {
			final EntityPersonProfile entityPersonProfile = new EntityPersonProfile(dtoPersonProfile, System.currentTimeMillis());

			mapPersonProfile.put(dtoPersonProfile.id(), entityPersonProfile);

			return Response.ok(entityPersonProfile).status(Response.Status.ACCEPTED).build();

		} else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<EntityPersonProfile> getAll() {
		final Predicate<Long, EntityPersonProfile> filteringPredicate = Predicates.and(
				Predicates.greaterEqual("age", 18),
				Predicates.lessEqual("age", 65));

		/*
		 * Get the last two using tsCreated.
		 * Selects only candidates between 18 and 65 years old.
		 */
		final PagingPredicate<Long, EntityPersonProfile> pagingPredicate = Predicates.pagingPredicate(filteringPredicate, new ComparatorPersonProfile(), 2);

		Collection<EntityPersonProfile> result = mapPersonProfile.values(pagingPredicate);

		return result;
	}

}
