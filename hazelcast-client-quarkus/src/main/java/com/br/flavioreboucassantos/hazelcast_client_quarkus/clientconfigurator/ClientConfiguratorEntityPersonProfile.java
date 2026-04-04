package com.br.flavioreboucassantos.hazelcast_client_quarkus.clientconfigurator;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer.SerializerPersonProfile;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NearCacheConfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClientConfiguratorEntityPersonProfile implements BaseClientConfigurator {

	private final Logger LOG = LoggerFactory.getLogger(ClientConfiguratorEntityPersonProfile.class);

	final String mapName;

	@Inject
	public ClientConfiguratorEntityPersonProfile(@ConfigProperty(name = "myApp.hazelcast.PersonProfile.mapName") final String mapName) {
		this.mapName = mapName;
	}

	@Override
	public String getMapName() {
		return mapName;
	}

	@Override
	public final void setClientConfig(final ClientConfig clientConfig) {
		/*
		 * 1) NearCacheConfig
		 */
		final NearCacheConfig nearCacheConfig = new NearCacheConfig(mapName)
				.setInMemoryFormat(InMemoryFormat.OBJECT) // Sets the data type used to store entries.
				.setInvalidateOnChange(true) // Sets if Near Cache entries are invalidated when the entries in the backing data structure are changed (updated or removed).
				.setTimeToLiveSeconds(3600) // timeToLiveSeconds the maximum number of seconds for each entry to stay in the Near Cache
				.setMaxIdleSeconds(600); // Set the maximum number of seconds each entry can stay in the Near Cache as untouched (not-read).

		/*
		 * 2) Configure EvictionConfig and attach to NearCacheConfig
		 */
		final EvictionConfig evictionConfigNearCacheConfig = new EvictionConfig();
		// Define a política de evicção
		evictionConfigNearCacheConfig.setEvictionPolicy(EvictionPolicy.LFU);
		/*
		 * Define o critério de tamanho:
		 * In Hazelcast, Near Cache eviction policies are strictly limited by the storage format.
		 * While IMap (the primary map) may support various heap policies,
		 * the Near Cache implementation has a more restricted set of supported max-size-policy options:
		 * - BINARY format: Only supports ENTRY_COUNT.
		 * - OBJECT format: Often used for performance to avoid deserialization, but also typically limited to ENTRY_COUNT.
		 * - NATIVE format (Enterprise): Supports USED_NATIVE_MEMORY_SIZE, USED_NATIVE_MEMORY_PERCENTAGE, etc.
		 */
		evictionConfigNearCacheConfig.setMaxSizePolicy(MaxSizePolicy.ENTRY_COUNT);
		// Define o tamanho máximo
		evictionConfigNearCacheConfig.setSize(100);
		// Attach EvictionConfig
		nearCacheConfig.setEvictionConfig(evictionConfigNearCacheConfig);

		/*
		 * Enterprise Edition:
		 * The Near Cache Preloader allows a client to persist the keys of its Near Cache to disk.
		 * Upon restart, the client uses these stored keys to eagerly re-populate the Near Cache from the cluster, significantly reducing the "cold start" latency for hot data sets.
		 */
//		final NearCachePreloaderConfig preloaderConfig = nearCacheConfig.getPreloaderConfig();
//		preloaderConfig.setDirectory("")
//				.setEnabled(true)
//				.setStoreInitialDelaySeconds(0)
//				.setStoreIntervalSeconds(0);

		/*
		 * 3) Configure CompactSerializationConfig and attach to SerializationConfig
		 */
		clientConfig.getSerializationConfig()
				.getCompactSerializationConfig()
				.addSerializer(new SerializerPersonProfile());

		/*
		 * 4) Attachs
		 */
		clientConfig.addNearCacheConfig(nearCacheConfig);
	}
}
