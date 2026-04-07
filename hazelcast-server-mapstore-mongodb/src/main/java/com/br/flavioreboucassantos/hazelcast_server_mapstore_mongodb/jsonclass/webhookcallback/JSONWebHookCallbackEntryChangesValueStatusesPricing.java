package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JSONWebHookCallbackEntryChangesValueStatusesPricing(
		@JsonProperty(value = "billable") boolean billable,
		@JsonProperty(value = "pricing_model") String pricingModel,
		@JsonProperty(value = "category") String category,
		@JsonProperty(value = "type") String type) {
}
