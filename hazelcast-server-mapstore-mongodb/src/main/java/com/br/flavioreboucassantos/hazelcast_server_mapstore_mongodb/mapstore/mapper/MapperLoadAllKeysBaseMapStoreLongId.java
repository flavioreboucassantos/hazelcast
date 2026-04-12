package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityLongId;
import com.mongodb.Function;

public final class MapperLoadAllKeysBaseMapStoreLongId<V extends BaseEntityLongId> implements Function<V, Long> {

	@Override
	public Long apply(final V t) {
		return t.id;
	}

}
