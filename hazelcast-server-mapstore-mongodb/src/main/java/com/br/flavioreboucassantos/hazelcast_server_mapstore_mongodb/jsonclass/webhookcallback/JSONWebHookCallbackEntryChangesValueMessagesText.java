package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValueMessagesText(@JsonProperty(value = "body") String body) {
}
