package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValueMessages(
		@JsonProperty(value = "from") String from,
		@JsonProperty(value = "id") String id,
		@JsonProperty(value = "timestamp") String timestamp,
		@JsonProperty(value = "type") String type,
		@JsonProperty(value = "text") JSONWebHookCallbackEntryChangesValueMessagesText text,
		@JsonProperty(value = "reaction") JSONWebHookCallbackEntryChangesValueMessagesReaction reaction) {

}
