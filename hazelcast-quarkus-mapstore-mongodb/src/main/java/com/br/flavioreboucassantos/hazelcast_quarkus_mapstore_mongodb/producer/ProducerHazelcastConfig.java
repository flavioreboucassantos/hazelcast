package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.producer;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.mapconfig.MapConfigBsonPersonProfile;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.serializer.BsonPersonProfileSerializer;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class ProducerHazelcastConfig {

	final MapConfigBsonPersonProfile mapConfigBsonPersonProfile;

	@Inject
	public ProducerHazelcastConfig(final MapConfigBsonPersonProfile mapConfigBsonPersonProfile) {
		this.mapConfigBsonPersonProfile = mapConfigBsonPersonProfile;
	}

	@Produces
	@ApplicationScoped
	public HazelcastInstance hazelcastInstance() {
		final MapConfig mapConfig = new MapConfig();

		mapConfigBsonPersonProfile.setConfig(mapConfig);
		/*
		 * 4) Configure Config and attach MapConfig
		 */
		Config config = new Config();
		// Attach MapConfig
		config.addMapConfig(mapConfig);

		/*
		 * 5) Compact Serialization:
		 * - Compact Serialization no Hazelcast (introduzida como estável na versão 5.2+) é altamente recomendado para melhorar o desempenho, reduzir o uso de memória e bandwidth, e suportar evolução de esquema (schema evolution) sem a
		 * necessidade de reescrever classes ou implementar interfaces de serialização pesadas.
		 * - Permitir que o Hazelcast use reflexão para serializar classes automaticamente.
		 * - Adicione a classe à configuração compacta.
		 */
//		config.getSerializationConfig()
//				.getCompactSerializationConfig()
//				.addClass(BsonPersonProfile.class);

		config.getSerializationConfig()
				.getCompactSerializationConfig()
				.addSerializer(new BsonPersonProfileSerializer());

		return Hazelcast.newHazelcastInstance(config);
	}
}
