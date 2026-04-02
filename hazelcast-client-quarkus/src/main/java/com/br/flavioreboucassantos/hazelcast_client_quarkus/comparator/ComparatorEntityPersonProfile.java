package com.br.flavioreboucassantos.hazelcast_client_quarkus.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map.Entry;

import com.br.flavioreboucassantos.hazelcast_client_quarkus.entity.EntityPersonProfile;

public final class ComparatorEntityPersonProfile implements Comparator<Entry<Long, EntityPersonProfile>>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6916266005255059731L;

	@Override
	public int compare(final Entry<Long, EntityPersonProfile> o1, final Entry<Long, EntityPersonProfile> o2) {
		// desc by tsCreated
		return Long.compare(o2.getValue().tsCreated, o1.getValue().tsCreated);
	}

}
