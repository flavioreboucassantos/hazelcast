package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValueMessagesReaction(
		@JsonProperty(value = "message_id") String messageId,
		@JsonProperty(value = "emoji") String emoji) {

}
