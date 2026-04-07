package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValueMetadata(
		@JsonProperty(value = "display_phone_number") String displayPhoneNumber,
		@JsonProperty(value = "phone_number_id") String phoneNumberId) {

}
