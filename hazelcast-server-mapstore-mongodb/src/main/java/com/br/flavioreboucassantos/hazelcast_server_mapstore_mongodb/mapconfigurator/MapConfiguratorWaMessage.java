package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.ConfigLoader;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaMessage;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.BaseMapStoreStringId;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer.SerializerWaMessage;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.config.MaxSizePolicy;
import com.mongodb.client.MongoDatabase;

public class MapConfiguratorWaMessage implements BaseMapConfigurator {

	final String mapName = ConfigLoader.getProperty("mapName.WaMessage"); // <----------
	final String collectionName = ConfigLoader.getProperty("collectionName.WaMessage"); // <----------

	@Override
	public String getMapName() {
		return mapName;
	}

	@Override
	public void setMapConfig(final MongoDatabase database, final Config config) {
		final MapConfig mapConfig = new MapConfig(mapName);

		mapConfig.setInMemoryFormat(InMemoryFormat.BINARY);

		final MapStoreConfig mapStoreConfig = new MapStoreConfig();
		mapStoreConfig.setImplementation(new BaseMapStoreStringId<>(EntityWaMessage.class, database, collectionName)); // <----------
		mapStoreConfig.setEnabled(true);
		mapStoreConfig.setWriteDelaySeconds(12);
		mapStoreConfig.setWriteBatchSize(100);
		mapStoreConfig.setWriteCoalescing(true);
		mapStoreConfig.setInitialLoadMode(InitialLoadMode.LAZY);
		mapConfig.setMapStoreConfig(mapStoreConfig);

		mapConfig.setStatisticsEnabled(true);

		mapConfig.addIndexConfig(new IndexConfig(IndexType.SORTED, "tsCreated"));

		final EvictionConfig evictionConfigMapConfig = new EvictionConfig();
		evictionConfigMapConfig.setEvictionPolicy(EvictionPolicy.LFU);
		evictionConfigMapConfig.setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE);
		evictionConfigMapConfig.setSize(100);
		mapConfig.setEvictionConfig(evictionConfigMapConfig);

		config.addMapConfig(mapConfig);

		config.getSerializationConfig()
				.setAllowOverrideDefaultSerializers(true)
				.getCompactSerializationConfig()
				.addSerializer(new SerializerWaMessage()); // <----------
	}

}
