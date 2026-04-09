package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityStringId;
import com.mongodb.Function;

public final class MapperLoadAllKeysBaseMapStoreStringId<T extends BaseEntityStringId> implements Function<T, String> {

	@Override
	public String apply(final T t) {
		return t.id;
	}

}
