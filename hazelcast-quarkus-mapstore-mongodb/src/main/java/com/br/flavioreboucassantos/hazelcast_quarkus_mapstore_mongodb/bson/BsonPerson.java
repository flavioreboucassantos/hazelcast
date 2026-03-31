package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson;

import org.bson.codecs.pojo.annotations.BsonId;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOBsonPerson;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "person")
public final class BsonPerson extends PanacheMongoEntityBase {

	@BsonId
	public String id;

	public String name;

	public int age;

	/*
	 * Please ensure the class has a public, empty constructor with no arguments, or else a constructor with a BsonCreator annotation.
	 */
	public BsonPerson() {
	}

//	@BsonCreator
	public BsonPerson(final DTOBsonPerson dtoBsonPerson) {
		id = dtoBsonPerson.id();
		name = dtoBsonPerson.name();
		age = dtoBsonPerson.age();
	}

}