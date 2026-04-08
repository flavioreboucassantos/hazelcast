package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.BaseEntityStringId;
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

public final class BaseMapStoreStringId<V extends BaseEntityStringId> implements MapStore<String, V> {
	private final ILogger LOG = Logger.getLogger(BaseMapStoreStringId.class);

	private final MongoCollection<V> collection;

	private final Bson projectionIncludeId = Projections.include("_id");
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

		LOG.info("load:: " + key);

		return collection.find(Filters.eq("_id", key)).first();
	}

	@Override
	public Map<String, V> loadAll(final Collection<String> keys) {

		LOG.info("loadAll::>> ");

		Map<String, V> map = collection.find(Filters.in("_id", keys))
				.into(new ArrayList<V>()) // Carrega os dados
				.stream()
				.collect(Collectors.toMap(keyMapperLoadAllBaseMapStoreStringId, valueMapperLoadAllBaseMapStore));

		LOG.info("loadAll:: " + keys.toString());

		return map;
	}

	@Override
	public Iterable<String> loadAllKeys() {

		LOG.info("loadAllKeys>> ");

		final List<String> ids = new ArrayList<String>();
		collection.find()
				.projection(projectionIncludeId)
				.map(mapperLoadAllKeysBaseMapStoreStringId)
				.into(ids);

		LOG.info("loadAllKeys:: " + ids.toString());

		return ids;
	}

	@Override
	public void store(final String key, final V value) {

		LOG.info("store:: " + key + value.toString());

		try {

			collection.replaceOne(Filters.eq("_id", value.id), value, replaceOptionsUpsertTrue);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void storeAll(final Map<String, V> map) {

		LOG.info("storeAll:: " + map.toString());

		map.forEach(this::store);
	}

	@Override
	public void delete(final String key) {

		LOG.info("delete:: " + key);

		collection.deleteOne(new Document("_id", key));
	}

	@Override
	public void deleteAll(final Collection<String> keys) {

		LOG.info("deleteAll:: " + keys);

		keys.forEach(this::delete);
	}
}
