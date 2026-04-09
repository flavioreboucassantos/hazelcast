package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityLongId;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper.KeyMapperLoadAllBaseMapStoreLongId;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper.MapperLoadAllKeysBaseMapStoreLongId;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper.ValueMapperLoadAllBaseMapStore;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.map.MapStore;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;

public final class BaseMapStoreLongId<V extends BaseEntityLongId> implements MapStore<Long, V> {
	private final ILogger LOG = Logger.getLogger(BaseMapStoreLongId.class);

	private final MongoCollection<V> collection;

	private final Bson projectionIncludeId = Projections.include("_id");
	private final ReplaceOptions replaceOptionsUpsertTrue;

	private final KeyMapperLoadAllBaseMapStoreLongId<V> keyMapperLoadAllBaseMapStoreLongId = new KeyMapperLoadAllBaseMapStoreLongId<V>();
	private final ValueMapperLoadAllBaseMapStore<V> valueMapperLoadAllBaseMapStore = new ValueMapperLoadAllBaseMapStore<V>();
	private final MapperLoadAllKeysBaseMapStoreLongId<V> mapperLoadAllKeysBaseMapStoreLongId = new MapperLoadAllKeysBaseMapStoreLongId<V>();

	public BaseMapStoreLongId(final Class<V> clazz, final MongoDatabase database, final String collectionName) {
		this.collection = database.getCollection(collectionName, clazz);

		final ReplaceOptions replaceOptions = new ReplaceOptions();
		replaceOptionsUpsertTrue = replaceOptions.upsert(true);
	}

	@Override
	public V load(final Long key) {

		LOG.info("load:: " + key);

		return collection.find(Filters.eq("_id", key)).first();
	}

	@Override
	public Map<Long, V> loadAll(final Collection<Long> keys) {

		LOG.info("loadAll::>> ");

		Map<Long, V> map = collection.find(Filters.in("_id", keys))
				.into(new ArrayList<V>()) // Carrega os dados
				.stream()
				.collect(Collectors.toMap(keyMapperLoadAllBaseMapStoreLongId, valueMapperLoadAllBaseMapStore));

		LOG.info("loadAll:: " + keys.toString());

		return map;
	}

	@Override
	public Iterable<Long> loadAllKeys() {

		LOG.info("loadAllKeys>> ");

		final List<Long> ids = new ArrayList<Long>();
		collection.find()
				.projection(projectionIncludeId)
				.map(mapperLoadAllKeysBaseMapStoreLongId)
				.into(ids);

		LOG.info("loadAllKeys:: " + ids.toString());

		return ids;
	}

	@Override
	public void store(final Long key, final V value) {

		LOG.info("store:: " + key + value.toString());

		try {

			collection.replaceOne(Filters.eq("_id", value.id), value, replaceOptionsUpsertTrue);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void storeAll(final Map<Long, V> map) {

		LOG.info("storeAll:: " + map.toString());

		map.forEach(this::store);
	}

	@Override
	public void delete(final Long key) {

		LOG.info("delete:: " + key);

		collection.deleteOne(new Document("_id", key));
	}

	@Override
	public void deleteAll(final Collection<Long> keys) {

		LOG.info("deleteAll:: " + keys);

		keys.forEach(this::delete);
	}
}
