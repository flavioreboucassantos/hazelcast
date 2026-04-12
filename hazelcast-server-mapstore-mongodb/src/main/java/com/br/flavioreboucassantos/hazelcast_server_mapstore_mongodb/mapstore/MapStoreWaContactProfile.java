package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import java.util.ArrayList;
import java.util.List;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaContactProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.base.BaseMapStoreLongId;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.mongodb.client.MongoDatabase;

public final class MapStoreWaContactProfile extends BaseMapStoreLongId<EntityWaContactProfile> {
	private final ILogger LOG = Logger.getLogger(MapStoreWaContactProfile.class);

	public MapStoreWaContactProfile(final MongoDatabase database, final String collectionName) {
		super(EntityWaContactProfile.class, database, collectionName);
	}

	@Override
	public final Iterable<Long> loadAllKeys() {

		// LOG.info("loadAllKeys>> ");

		final List<Long> ids = new ArrayList<Long>();
		collection.find()
				.projection(projectionIncludeOnlyId)
				.sort(sortsDescendingTsCreated)
//				.limit(100)
				.map(mapperLoadAllKeysBaseMapStoreLongId)
				.into(ids);

		// LOG.info("loadAllKeys:: " + ids.toString());

		LOG.info("\n\nloadAllKeys::ids.size()=" + ids.size());

		return ids;
	}

}
