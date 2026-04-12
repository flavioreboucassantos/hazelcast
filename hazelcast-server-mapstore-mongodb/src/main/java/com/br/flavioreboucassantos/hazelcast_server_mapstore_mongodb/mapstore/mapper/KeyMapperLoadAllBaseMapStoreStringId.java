package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import java.util.function.Function;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityStringId;

public final class KeyMapperLoadAllBaseMapStoreStringId<V extends BaseEntityStringId> implements Function<V, String> {

	@Override
	public String apply(final V t) {
		return t.id;
	}

}
