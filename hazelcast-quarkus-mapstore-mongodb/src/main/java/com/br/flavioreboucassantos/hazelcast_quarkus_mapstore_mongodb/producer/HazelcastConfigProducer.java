package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.producer;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.mapstore.BsonPersonMapStore;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.serializer.BsonPersonSerializer;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class HazelcastConfigProducer {

	final BsonPersonMapStore bsonPersonMapStore;

	@Inject
	public HazelcastConfigProducer(final BsonPersonMapStore bsonPersonMapStore) {
		this.bsonPersonMapStore = bsonPersonMapStore;
	}

	@Produces
	@ApplicationScoped
	public HazelcastInstance hazelcastInstance() {
		// Configure MapStore
		final MapStoreConfig mapStoreConfigBsonPerson = new MapStoreConfig();

		// Enabled for map
		mapStoreConfigBsonPerson.setImplementation(bsonPersonMapStore);
		mapStoreConfigBsonPerson.setEnabled(true);

		// The time in seconds to wait before writing entries to the data store (write-behind). A value of 0 means write-through.
		mapStoreConfigBsonPerson.setWriteDelaySeconds(0);

		// Used to create batches when writing to the map store. Only applicable in write-behind mode.
		mapStoreConfigBsonPerson.setWriteBatchSize(100);

		// If true and using write-behind, only the latest store operation on a key within the write-delay-seconds period will be reflected in the MapStore, coalescing multiple updates into one.
		mapStoreConfigBsonPerson.setWriteCoalescing(true);

		// Sets the initial entry loading mode, either LAZY (entries loaded when first accessed) or EAGER (all entries loaded on map initialization).
		mapStoreConfigBsonPerson.setInitialLoadMode(InitialLoadMode.EAGER);

		// A set of custom properties that can be passed to your MapStore implementation.
//		mapStoreConfigBsonPerson.setProperty("db.connection.url", "jdbc:postgresql://localhost:5432/mydb")
//				.setProperty("user", "myuser")
//				.setProperty("password", "mypass");

		// 2. Configure Map and attach Store
		final MapConfig mapConfigBsonPerson = new MapConfig("bsonPerson");
		mapConfigBsonPerson.setMapStoreConfig(mapStoreConfigBsonPerson);

		Config config = new Config();

		config.addMapConfig(mapConfigBsonPerson);

		/*
		 * Compact Serialization:
		 * - Compact Serialization no Hazelcast (introduzida como estável na versão 5.2+) é altamente recomendado para melhorar o desempenho, reduzir o uso de memória e bandwidth, e suportar evolução de esquema (schema evolution) sem a
		 * necessidade de reescrever classes ou implementar interfaces de serialização pesadas.
		 * - Permitir que o Hazelcast use reflexão para serializar classes automaticamente.
		 * - Adicione a classe à configuração compacta.
		 */
		config.getSerializationConfig()
				.getCompactSerializationConfig()
				.addClass(BsonPerson.class);

		config.getSerializationConfig()
				.getCompactSerializationConfig()
				.addSerializer(new BsonPersonSerializer());

		return Hazelcast.newHazelcastInstance(config);
	}
}
