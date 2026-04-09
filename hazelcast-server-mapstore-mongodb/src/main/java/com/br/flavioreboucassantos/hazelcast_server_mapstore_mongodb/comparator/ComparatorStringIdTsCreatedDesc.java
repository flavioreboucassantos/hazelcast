package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map.Entry;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityStringId;

public final class ComparatorStringIdTsCreatedDesc<V extends BaseEntityStringId> implements Comparator<Entry<String, V>>, Serializable {

	private static final long serialVersionUID = -6916266005255059731L;

	@Override
	public int compare(final Entry<String, V> o1, final Entry<String, V> o2) {
		// desc by tsCreated
		return Long.compare(o2.getValue().tsCreated, o1.getValue().tsCreated);
	}

}
