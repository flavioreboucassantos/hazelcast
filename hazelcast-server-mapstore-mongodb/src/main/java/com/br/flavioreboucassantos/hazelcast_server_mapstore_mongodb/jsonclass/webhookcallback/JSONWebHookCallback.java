package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallback(
		@JsonProperty(value = "object") String object,
		@JsonProperty(value = "entry") List<JSONWebHookCallbackEntry> entry) {

}
