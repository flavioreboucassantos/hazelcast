package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.bsonentity;

import java.io.Serializable;

public final class BsonPersonProfile implements Serializable {

	public long id;

	public String name;

	public int age;

	public long tsCreated;

	/*
	 * Please ensure the class has a public, empty constructor with no arguments, or else a constructor with a BsonCreator annotation.
	 */
	public BsonPersonProfile() {
	}

	public BsonPersonProfile(final long id, final String name, final int age, final long tsCreated) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.tsCreated = tsCreated;
	}

	@Override
	public String toString() {
		return "BsonPersonProfile [id=" + id + ", name=" + name + ", age=" + age + ", tsCreated=" + tsCreated + "]";
	}

}