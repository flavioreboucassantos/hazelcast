package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.bsonentity;

import java.io.Serializable;

public class BsonPersonFact implements Serializable {

	public long id;

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

}
