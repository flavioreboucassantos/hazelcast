package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValueContacts(
		@JsonProperty(value = "wa_id") String waId,
		@JsonProperty(value = "profile") JSONWebHookCallbackEntryChangesValueContactsProfile profile) {

}
