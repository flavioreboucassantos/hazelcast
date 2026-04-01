package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map.Entry;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity.BsonPersonProfile;

public final class ComparatorBsonPersonProfile implements Comparator<Entry<Long, BsonPersonProfile>>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3718693338054745223L;

	@Override
	public int compare(final Entry<Long, BsonPersonProfile> o1, final Entry<Long, BsonPersonProfile> o2) {
		// desc by tsCreated
		return Long.compare(o2.getValue().tsCreated, o1.getValue().tsCreated);
	}

}
