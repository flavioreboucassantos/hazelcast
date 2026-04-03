package com.br.flavioreboucassantos.hazelcast_client_quarkus.entity;

import com.br.flavioreboucassantos.hazelcast_client_quarkus.dto.DTOPersonProfile;

public final class EntityPersonProfile {

	public long id;

	public String name;

	public int age;

	public long tsCreated;

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
	 * @param dtoEntityPersonProfile
	 * @param tsCreated              is the Timestamp of the Creation.
	 */
	public EntityPersonProfile(final DTOPersonProfile dtoPersonProfile, final long tsCreated) {
		id = dtoPersonProfile.id();
		name = dtoPersonProfile.name();
		age = dtoPersonProfile.age();
		this.tsCreated = tsCreated;
	}

	@Override
	public String toString() {
		return "EntityPersonProfile [id=" + id + ", name=" + name + ", age=" + age + ", tsCreated=" + tsCreated + "]";
	}

}