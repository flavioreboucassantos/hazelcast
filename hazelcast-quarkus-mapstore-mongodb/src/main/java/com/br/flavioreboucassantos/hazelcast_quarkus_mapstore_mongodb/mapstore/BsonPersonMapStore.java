package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.mapstore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.repository.RepositoryPerson;
import com.hazelcast.map.MapStore;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BsonPersonMapStore implements MapStore<Long, BsonPerson> {

	private final Logger LOG = LoggerFactory.getLogger(BsonPersonMapStore.class);

	final RepositoryPerson repositoryPerson;

	@Inject
	public BsonPersonMapStore(final RepositoryPerson repositoryPerson) {
		this.repositoryPerson = repositoryPerson;
	}

	@Override
	public void store(final Long key, final BsonPerson value) {

		LOG.info("\n\n\nstore::" + key + " value: " + value.toString());

		// Write-Through: Hazelcast chama isso para persistir no Mongo
		repositoryPerson.persistOrUpdate(value);
	}

	@Override
	public void storeAll(final Map<Long, BsonPerson> map) {

		LOG.info("\n\n\nstoreAll::" + map);

		map.forEach(this::store);
	}

	@Override
	public BsonPerson load(final Long key) {

		LOG.info("\n\n\nload::" + key);

		// Carrega do Mongo se não estiver no cache
		return repositoryPerson.findById(key);
	}

	@Override
	public Map<Long, BsonPerson> loadAll(final Collection<Long> keys) {

		final Map<Long, BsonPerson> collect = keys.stream()
				.map(key -> repositoryPerson.findById(key))
				.filter(person -> person != null)
				.collect(Collectors.toMap(p -> p.id, p -> p));

		LOG.info("\n\n\nloadAll::" + keys + "\ncollect::" + collect.toString());

		return collect;
	}

	@Override
	public List<Long> loadAllKeys() {

		LOG.info("\n\n\nloadAllKeys>>");

		return repositoryPerson.streamAll()
				.map(p -> p.id)
				.collect(Collectors.toList());
	}

	@Override
	public void delete(final Long key) {
		repositoryPerson.deleteById(key);
	}

	@Override
	public void deleteAll(final Collection<Long> keys) {
		keys.forEach(this::delete);
	}
}