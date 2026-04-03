package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity;

import org.bson.codecs.pojo.annotations.BsonId;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.dto.DTOPersonProfile;

public final class EntityPersonProfile {

	@BsonId
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getTsCreated() {
		return tsCreated;
	}

	public void setTsCreated(long tsCreated) {
		this.tsCreated = tsCreated;
	}

	@Override
	public String toString() {
		return "EntityPersonProfile [id=" + id + ", name=" + name + ", age=" + age + ", tsCreated=" + tsCreated + "]";
	}

}