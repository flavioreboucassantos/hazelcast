package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import java.util.function.Function;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityLongId;

public final class KeyMapperLoadAllBaseMapStoreLongId<V extends BaseEntityLongId> implements Function<V, Long> {

	@Override
	public Long apply(final V t) {
		return t.id;
	}

}
