package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore;

import java.util.function.Function;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.BaseEntityLongId;

public final class KeyMapperLoadAllBaseMapStoreLongId<T extends BaseEntityLongId> implements Function<T, Long> {

	@Override
	public Long apply(final T t) {
		return t.id;
	}

}
