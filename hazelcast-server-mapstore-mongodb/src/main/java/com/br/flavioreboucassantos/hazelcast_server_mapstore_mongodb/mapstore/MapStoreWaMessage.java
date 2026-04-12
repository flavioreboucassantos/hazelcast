package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import java.util.ArrayList;
import java.util.List;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaMessage;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.base.BaseMapStoreStringId;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.mongodb.client.MongoDatabase;

public final class MapStoreWaMessage extends BaseMapStoreStringId<EntityWaMessage> {
	private final ILogger LOG = Logger.getLogger(MapStoreWaMessage.class);

	public MapStoreWaMessage(final MongoDatabase database, final String collectionName) {
		super(EntityWaMessage.class, database, collectionName);
	}

	@Override
	public Iterable<String> loadAllKeys() {

		// LOG.info("loadAllKeys>> ");

		final List<String> ids = new ArrayList<String>();
		collection.find()
				.projection(projectionIncludeOnlyId)
				.sort(sortsDescendingTsCreated)
//				.limit(2)
				.map(mapperLoadAllKeysBaseMapStoreStringId)
				.into(ids);

		// LOG.info("loadAllKeys:: " + ids.toString());
		
		LOG.info("\n\nloadAllKeys::ids.size()=" + ids.size());

		return ids;
	}

}
