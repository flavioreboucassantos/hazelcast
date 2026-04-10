package com.br.flavioreboucassantos.hazelcast_client_quarkus.controller;

import java.util.Collection;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_client_quarkus.service.BaseService;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator.ComparatorStringIdTsCreatedDesc;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaMessage;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/message")
public class ControllerEntityWaMessage {

	private final Logger LOG = LoggerFactory.getLogger(ControllerEntityWaMessage.class);

	final HazelcastInstance hazelcastInstance;
	final IMap<String, EntityWaMessage> mapWaMessage;

	final ComparatorStringIdTsCreatedDesc<EntityWaMessage> comparatorStringIdTsCreatedDesc = new ComparatorStringIdTsCreatedDesc<EntityWaMessage>();

	public ControllerEntityWaMessage(final HazelcastInstance hazelcastInstance,
			@ConfigProperty(name = "mapName.WaMessage") final String mapName) {
		this.hazelcastInstance = hazelcastInstance;
		mapWaMessage = hazelcastInstance.getMap(mapName);
	}

	@Path("/load")
	@POST
	public Response putLoad(@QueryParam("numberOfCopies") final int numberOfCopies) {
		if (numberOfCopies <= 0)
			return Response.ok("?numberOfCopies <= 0").build();

		for (int i = 0; i < numberOfCopies; i++) {

			final String id = BaseService.gerarStringAleatoria(128);

			mapWaMessage.put(id, new EntityWaMessage(id,
					BaseService.gerarStringAleatoria(16),
					BaseService.gerarStringAleatoria(16),
					"text",
					BaseService.gerarStringAleatoria(65536),
					System.currentTimeMillis()));
		}

		return Response.ok("-").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<EntityWaMessage> getAll() {
		final Predicate<Long, EntityPersonProfile> filteringPredicate = Predicates.and(
				Predicates.greaterEqual("tsCreated", 1),
				Predicates.lessEqual("tsCreated", 2));

		final PagingPredicate<String, EntityWaMessage> pagingPredicate = Predicates.pagingPredicate(comparatorStringIdTsCreatedDesc, 100);

		final Collection<EntityWaMessage> result = mapWaMessage.values(pagingPredicate);

		return result;
	}

}
