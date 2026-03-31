package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.repository;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

public final class RepositoryPerson implements PanacheMongoRepository<BsonPerson> {
}
