package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValueContactsProfile(
		@JsonProperty(value = "name") String name) {

}
