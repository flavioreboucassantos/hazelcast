package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity;

public final class EntityWaContactProfile extends BaseEntityLongId {

	public String name;

	public EntityWaContactProfile() {
	}

	public EntityWaContactProfile(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

}
