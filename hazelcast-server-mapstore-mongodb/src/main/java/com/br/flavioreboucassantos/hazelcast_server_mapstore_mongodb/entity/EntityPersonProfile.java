package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.dto.DTOPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.base.BaseEntityLongId;

public final class EntityPersonProfile extends BaseEntityLongId {

	public String name;

	public int age;

	// Please ensure the class has a public, empty constructor with no arguments, or else a constructor with a BsonCreator annotation
	public EntityPersonProfile() {
	}

	public EntityPersonProfile(final long id, final String name, final int age, final long tsCreated) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.tsCreated = tsCreated;
	}

	/**
	 *
	 * @param dtoPersonProfile
	 * @param tsCreated        is the Timestamp of the Creation.
	 */
	public EntityPersonProfile(final DTOPersonProfile dtoPersonProfile, final long tsCreated) {
		id = dtoPersonProfile.id();
		name = dtoPersonProfile.name();
		age = dtoPersonProfile.age();
		this.tsCreated = tsCreated;
	}

	@Override
	public String toString() {
		return "EntityPersonProfile [name=" + name + ", age=" + age + ", id=" + id + ", tsCreated=" + tsCreated + "]";
	}

}