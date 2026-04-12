package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityStringId;
import com.mongodb.Function;

public final class MapperLoadAllKeysBaseMapStoreStringId<V extends BaseEntityStringId> implements Function<V, String> {

	@Override
	public String apply(final V t) {
		return t.id;
	}

}
