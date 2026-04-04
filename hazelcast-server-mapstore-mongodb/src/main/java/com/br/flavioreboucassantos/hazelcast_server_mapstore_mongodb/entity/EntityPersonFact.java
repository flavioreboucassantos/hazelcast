package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.dto.DTOPersonFact;

public final class EntityPersonFact {

	public long id;

	public long idPersonProfile;

	public String description;

	public long tsCreated;

	public EntityPersonFact() {
	}

	public EntityPersonFact(final long id, final long idPersonProfile, final String description, final long tsCreated) {
		this.id = id;
		this.idPersonProfile = idPersonProfile;
		this.description = description;
		this.tsCreated = tsCreated;
	}

	public EntityPersonFact(final DTOPersonFact dtoPersonFact, final long tsCreated) {
		id = dtoPersonFact.id();
		idPersonProfile = dtoPersonFact.idPersonProfile();
		description = dtoPersonFact.description();
		this.tsCreated = tsCreated;
	}

	@Override
	public String toString() {
		return "EntityPersonFact [id=" + id + ", idPersonProfile=" + idPersonProfile + ", description=" + description + ", tsCreated=" + tsCreated + "]";
	}

}
