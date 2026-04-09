package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import java.util.function.Function;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityStringId;

public final class KeyMapperLoadAllBaseMapStoreStringId<T extends BaseEntityStringId> implements Function<T, String> {

	@Override
	public String apply(final T t) {
		return t.id;
	}

}
