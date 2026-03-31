package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.mapstore;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.repository.RepositoryPerson;
import com.hazelcast.map.MapStore;

import jakarta.inject.Inject;

public class BsonPersonMapStore implements MapStore<String, BsonPerson> {

	final RepositoryPerson repositoryPerson;

	@Inject
	public BsonPersonMapStore(final RepositoryPerson repositoryPerson) {
		this.repositoryPerson = repositoryPerson;
	}

	@Override
	public void store(final String key, final BsonPerson value) {
		// Write-Through: Hazelcast chama isso para persistir no Mongo
		repositoryPerson.persistOrUpdate(value);
	}

	@Override
	public void storeAll(final Map<String, BsonPerson> map) {
		map.forEach(this::store);
	}

	@Override
	public BsonPerson load(final String key) {
		// Carrega do Mongo se não estiver no cache
		if (ObjectId.isValid(key)) {
			return repositoryPerson.findById(new ObjectId(key));
		} else {
			// Handle the case where the string is not a valid ObjectId (e.g., return null, throw error)
			System.err.println("Invalid ObjectId format: " + key);
			return null;
		}
	}

	@Override
	public Map<String, BsonPerson> loadAll(final Collection<String> keys) {
		return repositoryPerson.stream("id", keys)
				.collect(Collectors.toMap(p -> p.id, p -> p));
	}

	@Override
	public Iterable<String> loadAllKeys() {
		return repositoryPerson.findAll().stream().map(p -> p.id).collect(Collectors.toList());
	}

	@Override
	public void delete(final String key) {
		if (ObjectId.isValid(key)) {
			repositoryPerson.deleteById(new ObjectId(key));
		} else {
			// Handle the case where the string is not a valid ObjectId (e.g., return null, throw error)
			System.err.println("Invalid ObjectId format: " + key);
		}
	}

	@Override
	public void deleteAll(Collection<String> keys) {
		keys.forEach(this::delete);
	}
}