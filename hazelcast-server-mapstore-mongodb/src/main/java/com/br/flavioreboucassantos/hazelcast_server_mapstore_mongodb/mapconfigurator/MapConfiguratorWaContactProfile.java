package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.ConfigLoader;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator.EvictionPolicyComparatorLongIdTsCreatedDesc;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.MapStoreWaContactProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer.SerializerWaContactProfile;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NearCacheConfig;
import com.mongodb.client.MongoDatabase;

public class MapConfiguratorWaContactProfile implements BaseMapConfigurator {

	final String mapName = ConfigLoader.getProperty("mapName.WaContactProfile"); // <----------
	final String collectionName = ConfigLoader.getProperty("collectionName.WaContactProfile"); // <----------

	@Override
	public String getMapName() {
		return mapName;
	}

	@Override
	public void setMapConfig(final MongoDatabase database, final Config config) {
		final MapConfig mapConfig = new MapConfig(mapName);

		final MapStoreConfig mapStoreConfig = new MapStoreConfig();
		mapStoreConfig.setImplementation(new MapStoreWaContactProfile(database, collectionName)); // <----------
		mapStoreConfig.setEnabled(true);
		mapStoreConfig.setWriteDelaySeconds(12);
		mapStoreConfig.setWriteBatchSize(100);
		mapStoreConfig.setWriteCoalescing(true);
		mapStoreConfig.setInitialLoadMode(InitialLoadMode.LAZY);
		mapConfig.setMapStoreConfig(mapStoreConfig);

		final NearCacheConfig nearCacheConfig = new NearCacheConfig(mapName)
				.setInMemoryFormat(InMemoryFormat.OBJECT)
				.setInvalidateOnChange(true)
				.setTimeToLiveSeconds(3600 * 10)
				.setMaxIdleSeconds(60 * 20);

		final EvictionConfig evictionConfig = new EvictionConfig();
		evictionConfig.setComparator(new EvictionPolicyComparatorLongIdTsCreatedDesc()); // <----------
		evictionConfig.setMaxSizePolicy(MaxSizePolicy.ENTRY_COUNT);
		evictionConfig.setSize(2);
		nearCacheConfig.setEvictionConfig(evictionConfig);

		mapConfig.setNearCacheConfig(nearCacheConfig);

		mapConfig.setStatisticsEnabled(true);

		final EvictionConfig evictionConfigMapConfig = new EvictionConfig();
		evictionConfigMapConfig.setEvictionPolicy(EvictionPolicy.LFU);
		evictionConfigMapConfig.setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE);
		evictionConfigMapConfig.setSize(100);
		mapConfig.setEvictionConfig(evictionConfigMapConfig);

		config.addMapConfig(mapConfig);

		config.getSerializationConfig()
				.setAllowOverrideDefaultSerializers(true)
				.getCompactSerializationConfig()
				.addSerializer(new SerializerWaContactProfile()); // <----------
	}

}
