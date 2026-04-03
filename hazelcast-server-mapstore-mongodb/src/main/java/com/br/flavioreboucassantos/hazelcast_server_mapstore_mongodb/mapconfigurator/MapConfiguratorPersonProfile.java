package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.MapStoreEntityPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer.SerializerEntityPersonProfile;
import com.hazelcast.config.Config;
import com.hazelcast.config.DataPersistenceConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.mongodb.client.MongoDatabase;

public class MapConfiguratorPersonProfile implements BaseMapConfigurator {

	private final ILogger LOG = Logger.getLogger(MapConfiguratorPersonProfile.class);

	final MongoDatabase database;
	final String mapName;

	public MapConfiguratorPersonProfile(final MongoDatabase database, final String mapName) {
		this.database = database;
		this.mapName = mapName;
	}

	@Override
	public String getMapName() {
		return mapName;
	}

	@Override
	public void setMapConfig(final Config config) {

		final MapConfig mapConfig = new MapConfig(mapName);

		/*
		 * 1) Configure MapStoreConfig to MapConfig
		 */
		final MapStoreConfig mapStoreConfig = new MapStoreConfig();
		// Sets the map store implementation object
		mapStoreConfig.setImplementation(new MapStoreEntityPersonProfile(database));
		// Enabled for map
		mapStoreConfig.setEnabled(true);
		// The time in seconds to wait before writing entries to the data store (write-behind). A value of 0 means write-through.
		mapStoreConfig.setWriteDelaySeconds(12);
		// Used to create batches when writing to the map store. Only applicable in write-behind mode.
		mapStoreConfig.setWriteBatchSize(100);
		// If true and using write-behind, only the latest store operation on a key within the write-delay-seconds period will be reflected in the MapStore, coalescing multiple updates into one.
		mapStoreConfig.setWriteCoalescing(true);
		// Sets the initial entry loading mode, either LAZY (entries loaded when first accessed) or EAGER (all entries loaded on map initialization).
		mapStoreConfig.setInitialLoadMode(InitialLoadMode.EAGER);
		// A set of custom properties that can be passed to your MapStore implementation.
//		mapStoreConfig.setProperty("quarkus.mongodb.hosts", "localhost:27017")
//				.setProperty("quarkus.mongodb.database", "db_hzc_mapstore");
//				.setProperty("user", "myuser")
//				.setProperty("password", "mypass");

		/*
		 * 2) Configure MapConfig and attach MapStoreConfig to MapConfig
		 */
		// Set to enable/disable map level statistics for this map
		mapConfig.setStatisticsEnabled(true);

		/*
		 * 2.1) DataPersistenceConfig
		 */
		final DataPersistenceConfig dataPersistenceConfig = new DataPersistenceConfig();
		dataPersistenceConfig.setEnabled(true);

		/*
		 * 2.2) Near Cache no MapConfig (Lado do Membro/Servidor)
		 * - Quando configurado no membro, o Near Cache é populado nos nós do cluster.
		 * - Isso é útil para lite members ou caches locais em servidores que acessam dados de outros membros.
		 */

		/*
		 * 2.9) Attachs
		 */
		// Attach DataPersistenceConfig
		mapConfig.setDataPersistenceConfig(dataPersistenceConfig);
		// Add Index to Map
		mapConfig.addIndexConfig(new IndexConfig(IndexType.SORTED, "tsCreated"));
		// Attach Store
		mapConfig.setMapStoreConfig(mapStoreConfig);

		/*
		 * 3) Configure EvictionConfig and attach to MapConfig
		 */
		final EvictionConfig evictionConfigMapConfig = new EvictionConfig();
		// Define a política de evicção
		evictionConfigMapConfig.setEvictionPolicy(EvictionPolicy.LFU);
		// Define o critério de tamanho
		evictionConfigMapConfig.setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE);
		// Define o tamanho máximo
		evictionConfigMapConfig.setSize(100);
		// Attach EvictionConfig
		mapConfig.setEvictionConfig(evictionConfigMapConfig);

		/*
		 * 4) Configure Config and attach MapConfig
		 */
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
//				.addClass(EntityPersonProfile.class); // To register a class for zero-config (reflection-based)

		config.getSerializationConfig()
				.setAllowOverrideDefaultSerializers(true)
				.getCompactSerializationConfig()
				.addSerializer(new SerializerEntityPersonProfile(mapName)); // To register an explicit serializer

	}
}
