package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityPersonProfile;
import com.mongodb.client.MongoDatabase;

public final class MapStorePersonProfile extends BaseMapStoreLongId<EntityPersonProfile> {

	public MapStorePersonProfile(final MongoDatabase database, final String collectionName) {
		super(database, collectionName);
	}

}