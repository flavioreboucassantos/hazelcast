package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson;

import org.bson.codecs.pojo.annotations.BsonId;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOBsonPerson;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "person")
public final class BsonPerson extends PanacheMongoEntityBase {

	@BsonId
	public String id;

	@BsonId
	public String name;

	@BsonId
	public int age;

	public BsonPerson(final DTOBsonPerson dtoBsonPerson) {
		id = dtoBsonPerson.id();
		name = dtoBsonPerson.name();
		age = dtoBsonPerson.age();
	}

}