package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntry(
		@JsonProperty(value = "id") String id,
		@JsonProperty(value = "changes") List<JSONWebHookCallbackEntryChanges> changes) {
}
