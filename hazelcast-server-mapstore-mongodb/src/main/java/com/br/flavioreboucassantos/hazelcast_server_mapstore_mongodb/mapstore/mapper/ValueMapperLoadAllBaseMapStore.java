package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.mapper;

import java.util.function.Function;

public final class ValueMapperLoadAllBaseMapStore<T> implements Function<T, T> {

	@Override
	public T apply(final T t) {
		return t;
	}

}
