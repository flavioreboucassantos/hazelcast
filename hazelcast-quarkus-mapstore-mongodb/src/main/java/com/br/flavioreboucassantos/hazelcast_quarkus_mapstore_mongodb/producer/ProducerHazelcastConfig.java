package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.producer;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.mapstore.BsonPersonMapStore;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.serializer.BsonPersonSerializer;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class ProducerHazelcastConfig {

	final BsonPersonMapStore bsonPersonMapStore;

	@Inject
	public ProducerHazelcastConfig(final BsonPersonMapStore bsonPersonMapStore) {
		this.bsonPersonMapStore = bsonPersonMapStore;
	}

	@Produces
	@ApplicationScoped
	public HazelcastInstance hazelcastInstance() {
		/*
		 * 1) Configure MapStoreConfig to MapConfig
		 */
		final MapStoreConfig mapStoreConfig = new MapStoreConfig();
		// Sets the map store implementation object
		mapStoreConfig.setImplementation(bsonPersonMapStore);
		// Enabled for map
		mapStoreConfig.setEnabled(true);
		// The time in seconds to wait before writing entries to the data store (write-behind). A value of 0 means write-through.
		mapStoreConfig.setWriteDelaySeconds(0);
		// Used to create batches when writing to the map store. Only applicable in write-behind mode.
		mapStoreConfig.setWriteBatchSize(100);
		// If true and using write-behind, only the latest store operation on a key within the write-delay-seconds period will be reflected in the MapStore, coalescing multiple updates into one.
		mapStoreConfig.setWriteCoalescing(true);
		// Sets the initial entry loading mode, either LAZY (entries loaded when first accessed) or EAGER (all entries loaded on map initialization).
		mapStoreConfig.setInitialLoadMode(InitialLoadMode.EAGER);
		// A set of custom properties that can be passed to your MapStore implementation.
//		mapStoreConfigBsonPerson.setProperty("db.connection.url", "jdbc:postgresql://localhost:5432/mydb")
//				.setProperty("user", "myuser")
//				.setProperty("password", "mypass");

		/*
		 * 2) Configure MapConfig and attach MapStoreConfig to MapConfig
		 */
		final MapConfig mapConfig = new MapConfig("bsonPerson");
		/*
		 * 2.1) Configure IndexConfig and attach to MapConfig
		 */
		// Add Index to Map
		mapConfig.addIndexConfig(new IndexConfig(IndexType.SORTED, "_created"));
		// Attach Store
		mapConfig.setMapStoreConfig(mapStoreConfig);

		/*
		 * 3) Configure EvictionConfig and attach to MapConfig
		 */
		final EvictionConfig evictionConfig = new EvictionConfig();
		// Define a política de evicção
		evictionConfig.setEvictionPolicy(EvictionPolicy.LFU);
		// Define o critério de tamanho
		evictionConfig.setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE);
		// Define o tamanho máximo
		evictionConfig.setSize(100);
		// Attach EvictionConfig
		mapConfig.setEvictionConfig(evictionConfig);

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
//				.addClass(BsonPerson.class);

		config.getSerializationConfig()
				.getCompactSerializationConfig()
				.addSerializer(new BsonPersonSerializer());

		return Hazelcast.newHazelcastInstance(config);
	}
}
