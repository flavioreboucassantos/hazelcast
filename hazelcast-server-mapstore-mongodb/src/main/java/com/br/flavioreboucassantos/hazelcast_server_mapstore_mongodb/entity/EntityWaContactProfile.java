package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityLongId;

public final class EntityWaContactProfile extends BaseEntityLongId {

	public String name;

	public EntityWaContactProfile() {
	}

	public EntityWaContactProfile(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "EntityWaContactProfile [name=" + name + ", id=" + id + ", tsCreated=" + tsCreated + "]";
	}

}
