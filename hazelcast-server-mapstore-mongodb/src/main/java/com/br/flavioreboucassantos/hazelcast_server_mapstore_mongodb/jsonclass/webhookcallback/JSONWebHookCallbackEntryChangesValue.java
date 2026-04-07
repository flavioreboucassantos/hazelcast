package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValue(
		@JsonProperty(value = "messaging_product") String messagingProduct,
		@JsonProperty(value = "metadata") JSONWebHookCallbackEntryChangesValueMetadata metadata,
		@JsonProperty(value = "contacts") List<JSONWebHookCallbackEntryChangesValueContacts> contacts,
		@JsonProperty(value = "messages") List<JSONWebHookCallbackEntryChangesValueMessages> messages,
		@JsonProperty(value = "statuses") List<JSONWebHookCallbackEntryChangesValueStatuses> statuses) {

}
