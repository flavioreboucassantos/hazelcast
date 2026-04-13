package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.ConfigLoader;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator.EvictionPolicyComparatorLongIdTsCreatedDesc;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.MapStorePersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer.SerializerPersonProfile;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NearCacheConfig;
import com.mongodb.client.MongoDatabase;

public class MapConfiguratorPersonProfile implements BaseMapConfigurator {

	final String mapName = ConfigLoader.getProperty("mapName.PersonProfile"); // <----------
	final String collectionName = ConfigLoader.getProperty("collectionName.PersonProfile"); // <----------

	@Override
	public String getMapName() {
		return mapName;
	}

	@Override
	public void setMapConfig(final MongoDatabase database, final Config config) {

		final MapConfig mapConfig = new MapConfig(mapName);
		// Set to enable/disable map level statistics for this map
		mapConfig.setStatisticsEnabled(true);
		// Add Index to Map
		mapConfig.addIndexConfig(new IndexConfig(IndexType.SORTED, "tsCreated"));

		/*
		 * 1) Configure MapStoreConfig to MapConfig
		 */
		final MapStoreConfig mapStoreConfig = new MapStoreConfig();
		// Sets the map store implementation object
		mapStoreConfig.setImplementation(new MapStorePersonProfile(database, collectionName)); // <----------
		// Enabled for map
		mapStoreConfig.setEnabled(true);
		// The time in seconds to wait before writing entries to the data store (write-behind). A value of 0 means write-through.
		mapStoreConfig.setWriteDelaySeconds(12);
		// Used to create batches when writing to the map store. Only applicable in write-behind mode.
		mapStoreConfig.setWriteBatchSize(100);
		// If true and using write-behind, only the latest store operation on a key within the write-delay-seconds period will be reflected in the MapStore, coalescing multiple updates into one.
		mapStoreConfig.setWriteCoalescing(true);
		// Sets the initial entry loading mode, either LAZY (entries loaded when first accessed) or EAGER (all entries loaded on map initialization).
		mapStoreConfig.setInitialLoadMode(InitialLoadMode.LAZY);
		// A set of custom properties that can be passed to your MapStore implementation.
//		mapStoreConfig.setProperty("quarkus.mongodb.hosts", "localhost:27017")
//				.setProperty("quarkus.mongodb.database", "db_hzc_mapstore");
//				.setProperty("user", "myuser")
//				.setProperty("password", "mypass");
		// Attach Store
		mapConfig.setMapStoreConfig(mapStoreConfig);

		/*
		 * 2) Configure NearCacheConfig and EvictionConfig and Attach to MapConfig
		 * A principal diferença entre definir o EvictionConfig (evicção) no MapConfig e no NearCacheConfig no Hazelcast reside no escopo da cache (global vs. local) e no objetivo da gestão de memória.
		 * MapConfig (Evicção Global/Cluster): Gerencia a memória de todo o cluster. Quando uma entrada é evictada (removida) aqui, ela desaparece de todos os membros do cluster.
		 * NearCacheConfig (Evicção Local/Cliente): Gerencia a memória local do cliente ou do nó que está lendo os dados. A evicção aqui remove apenas a cópia local, sem afetar os dados no cluster principal.
		 */
		final NearCacheConfig nearCacheConfig = new NearCacheConfig(mapName)
				.setInMemoryFormat(InMemoryFormat.OBJECT)
				.setInvalidateOnChange(true)
				.setTimeToLiveSeconds(3600 * 10)
				.setMaxIdleSeconds(60 * 20);
		final EvictionConfig evictionConfig = new EvictionConfig();
		evictionConfig.setComparator(new EvictionPolicyComparatorLongIdTsCreatedDesc()); // <----------
		/*
		 * Substituição da Lógica:
		 * Quando você define uma classe customizada que implementa EvictionPolicyComparator e a configura no EvictionConfig,
		 * o Hazelcast utiliza essa classe para decidir qual entrada remover,
		 * ignorando a política eviction-policy (LRU/LFU/RANDOM) definida.
		 */
//		evictionConfig.setEvictionPolicy(EvictionPolicy.LRU);
		evictionConfig.setMaxSizePolicy(MaxSizePolicy.ENTRY_COUNT);
		evictionConfig.setSize(2);
		nearCacheConfig.setEvictionConfig(evictionConfig);
		// Attach NearCacheConfig
		mapConfig.setNearCacheConfig(nearCacheConfig);

		/*
		 * 3) Set Data Persistence Config
		 */
//		final DataPersistenceConfig dataPersistenceConfig = new DataPersistenceConfig();
//		dataPersistenceConfig.setEnabled(true);
//		mapConfig.setDataPersistenceConfig(dataPersistenceConfig);	

		/*
		 * 4) Attach MapConfig
		 */
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
//				.addClass(EntityPersonProfile.class); // To register a class for zero-config (reflection-based)

		config.getSerializationConfig()
				.setAllowOverrideDefaultSerializers(true)
				.getCompactSerializationConfig()
				// To register an explicit serializer
				.addSerializer(new SerializerPersonProfile()); // <----------

	}
}
