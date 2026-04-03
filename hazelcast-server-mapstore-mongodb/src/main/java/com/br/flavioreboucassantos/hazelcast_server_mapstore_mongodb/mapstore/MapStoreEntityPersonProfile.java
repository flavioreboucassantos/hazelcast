package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityPersonProfile;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.map.MapStore;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;

import tools.jackson.databind.ObjectMapper;

public final class MapStoreEntityPersonProfile implements MapStore<Long, EntityPersonProfile> {

	private final ILogger LOG = Logger.getLogger(MapStoreEntityPersonProfile.class);

	private final MongoCollection<EntityPersonProfile> collection;
	private final MongoCollection<Document> collectionDocument;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final Bson projectionIncludeId = Projections.include("_id");
	private final ReplaceOptions replaceOptionsUpsertTrue;

	public MapStoreEntityPersonProfile(final MongoDatabase database) {
		this.collection = database.getCollection("person_profile", EntityPersonProfile.class);
		this.collectionDocument = database.getCollection("person_profile", Document.class);

		ReplaceOptions replaceOptions = new ReplaceOptions();
		replaceOptionsUpsertTrue = replaceOptions.upsert(true);
	}

	@Override
	public EntityPersonProfile load(final Long key) {

		LOG.info("load:: " + key);

		return collection.find(Filters.eq("_id", key)).first();
	}

	@Override
	public Map<Long, EntityPersonProfile> loadAll(final Collection<Long> keys) {

		LOG.info("loadAll::>> ");

		Map<Long, EntityPersonProfile> map = collection.find(Filters.in("_id", keys))
				.into(new ArrayList<>()) // Carrega os dados
				.stream()
				.collect(Collectors.toMap(b -> b.id, b -> b));

		LOG.info("loadAll:: " + keys.toString());

		return map;
	}

	@Override
	public Iterable<Long> loadAllKeys() {

		LOG.info("loadAllKeys>> ");

		final List<Long> ids = new ArrayList<>();
		collection.find()
				.projection(projectionIncludeId)
				.map(doc -> doc.id)
				.into(ids);

		LOG.info("loadAllKeys:: " + ids.toString());

		return ids;
	}

	@Override
	public void store(final Long key, final EntityPersonProfile value) {

		LOG.info("store:: " + key + value.toString());

		try {

			collection.replaceOne(Filters.eq("_id", value.id), value, replaceOptionsUpsertTrue);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void storeAll(final Map<Long, EntityPersonProfile> map) {

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