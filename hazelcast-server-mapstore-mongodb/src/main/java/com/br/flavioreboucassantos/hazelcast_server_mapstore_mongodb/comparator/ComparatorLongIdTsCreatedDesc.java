package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map.Entry;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityLongId;

public final class ComparatorLongIdTsCreatedDesc<V extends BaseEntityLongId> implements Comparator<Entry<Long, V>>, Serializable {

	private static final long serialVersionUID = -6916266005255059731L;

	@Override
	public int compare(final Entry<Long, V> o1, final Entry<Long, V> o2) {
		// desc by tsCreated
		return Long.compare(o2.getValue().tsCreated, o1.getValue().tsCreated);
	}

}
