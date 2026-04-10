package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityStringId;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper.KeyMapperLoadAllBaseMapStoreStringId;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper.MapperLoadAllKeysBaseMapStoreStringId;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper.ValueMapperLoadAllBaseMapStore;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.map.MapStore;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;

public final class BaseMapStoreStringId<V extends BaseEntityStringId> implements MapStore<String, V> {
	private final ILogger LOG = Logger.getLogger(BaseMapStoreStringId.class);

	private final MongoCollection<V> collection;

	private final Bson projectionIncludeId = Projections.include("_id");
	private final Bson sortsDescendingTsCreated = Sorts.descending("tsCreated");
	private final ReplaceOptions replaceOptionsUpsertTrue;

	private final KeyMapperLoadAllBaseMapStoreStringId<V> keyMapperLoadAllBaseMapStoreStringId = new KeyMapperLoadAllBaseMapStoreStringId<V>();
	private final ValueMapperLoadAllBaseMapStore<V> valueMapperLoadAllBaseMapStore = new ValueMapperLoadAllBaseMapStore<V>();
	private final MapperLoadAllKeysBaseMapStoreStringId<V> mapperLoadAllKeysBaseMapStoreStringId = new MapperLoadAllKeysBaseMapStoreStringId<V>();

	public BaseMapStoreStringId(final Class<V> clazz, final MongoDatabase database, final String collectionName) {
		this.collection = database.getCollection(collectionName, clazz);

		final ReplaceOptions replaceOptions = new ReplaceOptions();
		replaceOptionsUpsertTrue = replaceOptions.upsert(true);
	}

	@Override
	public V load(final String key) {

		// LOG.info("load:: " + key);

		return collection.find(Filters.eq("_id", key)).first();
	}

	@Override
	public Map<String, V> loadAll(final Collection<String> keys) {

		// LOG.info("\nloadAll::>>keys.size()=" + keys.size());

		Map<String, V> map = collection.find(Filters.in("_id", keys))
				.into(new ArrayList<V>()) // Carrega os dados
				.stream()
				.collect(Collectors.toMap(keyMapperLoadAllBaseMapStoreStringId, valueMapperLoadAllBaseMapStore));

		// LOG.info("loadAll:: " + keys.toString());

		return map;
	}

	@Override
	public Iterable<String> loadAllKeys() {

		// LOG.info("loadAllKeys>> ");

		final List<String> ids = new ArrayList<String>();
		collection.find()
				.projection(projectionIncludeId)
				.sort(sortsDescendingTsCreated)
				// -> BEFORE GET 100
				// abr. 10, 2026 6:29:33 PM com.hazelcast.internal.diagnostics.HealthMonitor
				// INFO: [192.168.0.66]:5701 [dev] [5.6.0] processors=8, physical.memory.total=15,9G, physical.memory.free=5,7G, swap.space.total=0, swap.space.free=0, heap.memory.used=432,6M, heap.memory.free=79,4M, heap.memory.total=512,0M, heap.memory.max=512,0M, heap.memory.used/total=84,49%, heap.memory.used/max=84,49%, minor.gc.count=118, minor.gc.time=515ms, major.gc.count=8, major.gc.time=314ms, unknown.gc.count=44, unknown.gc.time=68ms, load.process=0,00%, load.system=5,94%, load.systemAverage=n/a thread.count=97, thread.peakCount=98, cluster.timeDiff=0, event.q.size=0, executor.q.async.size=0, executor.q.client.size=0, executor.q.client.query.size=0, executor.q.client.blocking.size=0, executor.q.query.size=0, executor.q.scheduled.size=0, executor.q.io.size=0, executor.q.system.size=0, executor.q.operations.size=0, executor.q.priorityOperation.size=0, operations.completed.count=5220, executor.q.mapLoad.size=0, executor.q.mapLoadAllKeys.size=0, executor.q.cluster.size=0, executor.q.response.size=0, operations.running.count=0, operations.pending.invocations.percentage=0,00%, operations.pending.invocations.count=2, proxy.count=8, clientEndpoint.count=0, connection.active.count=0, client.connection.count=0, connection.count=0
				// -> AFTER GET 100
				// SEVERE: [192.168.0.66]:5701 [dev] [5.6.0] Java heap space
				// java.lang.OutOfMemoryError: Java heap space
				// at com.hazelcast.instance.impl.OutOfMemoryErrorDispatcher.onOutOfMemory(OutOfMemoryErrorDispatcher.java:169)
				// at com.hazelcast.internal.serialization.impl.SerializationUtil.handleException(SerializationUtil.java:101)
				// at com.hazelcast.internal.serialization.impl.AbstractSerializationService.toObject(AbstractSerializationService.java:276)
				// at com.hazelcast.query.impl.CachedQueryEntry.getValue(CachedQueryEntry.java:117)
				.limit(10000)
				.map(mapperLoadAllKeysBaseMapStoreStringId)
				.into(ids);

		// LOG.info("loadAllKeys:: " + ids.toString());

		LOG.info("\n\nloadAllKeys::ids.size()=" + ids.size());

		return ids;
	}

	@Override
	public void store(final String key, final V value) {

		// LOG.info("store:: " + key + value.toString());

		try {

			collection.replaceOne(Filters.eq("_id", value.id), value, replaceOptionsUpsertTrue);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void storeAll(final Map<String, V> map) {

		// LOG.info("storeAll:: " + map.toString());

		map.forEach(this::store);
	}

	@Override
	public void delete(final String key) {

		// LOG.info("delete:: " + key);

		collection.deleteOne(new Document("_id", key));
	}

	@Override
	public void deleteAll(final Collection<String> keys) {

		// LOG.info("deleteAll:: " + keys);

		keys.forEach(this::delete);
	}
}
