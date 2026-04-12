package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.base;

import java.util.ArrayList;
import java.util.Collection;
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
import com.mongodb.client.model.Sorts;

public abstract class BaseMapStoreLongId<V extends BaseEntityLongId> implements MapStore<Long, V> {
	private final ILogger LOG = Logger.getLogger(BaseMapStoreLongId.class);

	protected final MongoCollection<V> collection;

	protected final Bson projectionIncludeOnlyId = Projections.include("_id");
	protected final Bson sortsDescendingTsCreated = Sorts.descending("tsCreated");
	protected final Bson sortsAscendingTsCreated = Sorts.ascending("tsCreated");

	protected final ReplaceOptions replaceOptionsUpsertTrue;

	protected final KeyMapperLoadAllBaseMapStoreLongId<V> keyMapperLoadAllBaseMapStoreLongId = new KeyMapperLoadAllBaseMapStoreLongId<V>();
	protected final ValueMapperLoadAllBaseMapStore<V> valueMapperLoadAllBaseMapStore = new ValueMapperLoadAllBaseMapStore<V>();
	protected final MapperLoadAllKeysBaseMapStoreLongId<V> mapperLoadAllKeysBaseMapStoreLongId = new MapperLoadAllKeysBaseMapStoreLongId<V>();

	public BaseMapStoreLongId(final Class<V> clazz, final MongoDatabase database, final String collectionName) {
		this.collection = database.getCollection(collectionName, clazz);

		final ReplaceOptions replaceOptions = new ReplaceOptions();
		replaceOptionsUpsertTrue = replaceOptions.upsert(true);
	}

	@Override
	public final V load(final Long key) {

		LOG.info("load:: " + key);

		return collection.find(Filters.eq("_id", key)).first();
	}

	@Override
	public final Map<Long, V> loadAll(final Collection<Long> keys) {

		LOG.info("loadAll::>> ");

		Map<Long, V> map = collection.find(Filters.in("_id", keys))
				.into(new ArrayList<V>()) // Carrega os dados
				.stream()
				.collect(Collectors.toMap(keyMapperLoadAllBaseMapStoreLongId, valueMapperLoadAllBaseMapStore));

		LOG.info("loadAll:: " + keys.toString());

		return map;
	}

	@Override
	public final void store(final Long key, final V value) {

		LOG.info("store:: " + key + value.toString());

		try {

			collection.replaceOne(Filters.eq("_id", value.id), value, replaceOptionsUpsertTrue);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void storeAll(final Map<Long, V> map) {

		LOG.info("storeAll:: " + map.toString());

		map.forEach(this::store);
	}

	@Override
	public final void delete(final Long key) {

		LOG.info("delete:: " + key);

		collection.deleteOne(new Document("_id", key));
	}

	@Override
	public final void deleteAll(final Collection<Long> keys) {

		LOG.info("deleteAll:: " + keys);

		keys.forEach(this::delete);
	}
}
