package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity;

public class EntityWaMessage {

	public String id;
	public String displayPhoneNumber;
	public String from;
	public String type;
	public String body;
	public long tsCreated;

	public EntityWaMessage() {
	}

	public EntityWaMessage(final String id, final String displayPhoneNumber, final String from, final String type, final String body, final long tsCreated) {
		this.id = id;
		this.displayPhoneNumber = displayPhoneNumber;
		this.from = from;
		this.type = type;
		this.body = body;
		this.tsCreated = tsCreated;
	}

}
