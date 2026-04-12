package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityTsCreated;
import com.hazelcast.spi.eviction.EvictableEntryView;
import com.hazelcast.spi.eviction.EvictionPolicyComparator;

public class EvictionPolicyComparatorLongIdTsCreatedDesc implements EvictionPolicyComparator<Long, BaseEntityTsCreated, EvictableEntryView<Long, BaseEntityTsCreated>> {

	private static final long serialVersionUID = 4171160534980334742L;

	@Override
	public int compare(final EvictableEntryView<Long, BaseEntityTsCreated> o1, final EvictableEntryView<Long, BaseEntityTsCreated> o2) {
		// desc by tsCreated
		return Long.compare(o2.getValue().tsCreated, o1.getValue().tsCreated);
	}
}