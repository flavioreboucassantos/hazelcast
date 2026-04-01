package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity;

import org.bson.codecs.pojo.annotations.BsonId;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOBsonPersonProfile;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "person_profile")
public final class BsonPersonProfile extends PanacheMongoEntityBase {

	@BsonId
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

	/**
	 *
	 * @param dtoBsonPersonProfile
	 * @param tsCreated            is the Timestamp of the Creation.
	 */
	public BsonPersonProfile(final DTOBsonPersonProfile dtoBsonPersonProfile, final long tsCreated) {
		id = dtoBsonPersonProfile.id();
		name = dtoBsonPersonProfile.name();
		age = dtoBsonPersonProfile.age();
		this.tsCreated = tsCreated;
	}

	@Override
	public String toString() {
		return "BsonPersonProfile [id=" + id + ", name=" + name + ", age=" + age + ", tsCreated=" + tsCreated + "]";
	}

}