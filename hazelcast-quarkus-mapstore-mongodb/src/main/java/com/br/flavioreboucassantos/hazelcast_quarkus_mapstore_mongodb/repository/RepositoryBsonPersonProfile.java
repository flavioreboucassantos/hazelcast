package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.repository;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity.BsonPersonProfile;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class RepositoryBsonPersonProfile implements PanacheMongoRepositoryBase<BsonPersonProfile, Long> {
}
