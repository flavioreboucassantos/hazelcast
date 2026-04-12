package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.comparator;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityTsCreated;
import com.hazelcast.spi.eviction.EvictableEntryView;
import com.hazelcast.spi.eviction.EvictionPolicyComparator;

public final class EvictionPolicyComparatorStringIdTsCreatedDesc implements EvictionPolicyComparator<String, BaseEntityTsCreated, EvictableEntryView<String, BaseEntityTsCreated>> {

	private static final long serialVersionUID = -775873183872651566L;

	@Override
	public int compare(final EvictableEntryView<String, BaseEntityTsCreated> o1, final EvictableEntryView<String, BaseEntityTsCreated> o2) {
		// desc by tsCreated
		return Long.compare(o2.getValue().tsCreated, o1.getValue().tsCreated);
	}

}
