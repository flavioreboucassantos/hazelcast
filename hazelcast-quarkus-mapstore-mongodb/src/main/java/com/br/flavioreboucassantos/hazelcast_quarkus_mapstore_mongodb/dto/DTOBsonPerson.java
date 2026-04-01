package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto;

public record DTOBsonPerson(
		long id,
		String name,
		int age,
		long _created) {
}
