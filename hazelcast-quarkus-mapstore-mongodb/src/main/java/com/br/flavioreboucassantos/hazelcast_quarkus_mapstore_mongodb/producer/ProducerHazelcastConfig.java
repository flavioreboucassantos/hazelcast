package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.producer;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.mapconfig.MapConfigBsonPersonProfile;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class ProducerHazelcastConfig {

	@Produces
	@ApplicationScoped
	public HazelcastInstance hazelcastInstance(final MapConfigBsonPersonProfile mapConfigBsonPersonProfile) {
		Config config = new Config();

		mapConfigBsonPersonProfile.setConfig(config);

		return Hazelcast.newHazelcastInstance(config);
	}

}
