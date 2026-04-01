package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity;

import org.bson.codecs.pojo.annotations.BsonId;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOPersonFact;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "person_fact")
public class BsonPersonFact extends PanacheMongoEntityBase {

	@BsonId
	public long id;

	@BsonId
	public long idBsonPersonProfile;

	public String description;

	public long tsCreated;

	public BsonPersonFact() {
	}

	public BsonPersonFact(final long id, final long idBsonPersonProfile, final String description, final long tsCreated) {
		this.id = id;
		this.idBsonPersonProfile = idBsonPersonProfile;
		this.description = description;
		this.tsCreated = tsCreated;
	}

	public BsonPersonFact(final DTOPersonFact dtoPersonFact, final long tsCreated) {
		id = dtoPersonFact.id();
		idBsonPersonProfile = dtoPersonFact.idPersonProfile();
		description = dtoPersonFact.description();
		this.tsCreated = tsCreated;
	}

}
