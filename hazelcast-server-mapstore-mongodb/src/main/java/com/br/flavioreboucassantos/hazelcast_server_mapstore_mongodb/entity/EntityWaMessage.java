package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity;

public class EntityWaMessage {

	String id;
	String displayPhoneNumber;
	String from;
	String type;
	String body;
	long tsCreated;

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
