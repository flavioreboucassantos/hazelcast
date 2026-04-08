package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.BaseEntityLongId;
import com.mongodb.Function;

public final class MapperLoadAllKeysBaseMapStoreLongId<T extends BaseEntityLongId> implements Function<T, Long> {

	@Override
	public Long apply(final T t) {
		return t.id;
	}

}
