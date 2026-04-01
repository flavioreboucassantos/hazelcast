package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.mapstore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity.BsonPersonProfile;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.repository.RepositoryBsonPersonProfile;
import com.hazelcast.map.MapStore;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BsonPersonProfileMapStore implements MapStore<Long, BsonPersonProfile> {

	private final Logger LOG = LoggerFactory.getLogger(BsonPersonProfileMapStore.class);

	final RepositoryBsonPersonProfile repositoryBsonPersonProfile;

	@Inject
	public BsonPersonProfileMapStore(final RepositoryBsonPersonProfile repositoryBsonPersonProfile) {
		this.repositoryBsonPersonProfile = repositoryBsonPersonProfile;
	}

	@Override
	public void store(final Long key, final BsonPersonProfile value) {

		LOG.info("\n\n\nstore::" + key + " value: " + value.toString());

		// Write-Through: Hazelcast chama isso para persistir no Mongo
		repositoryBsonPersonProfile.persistOrUpdate(value);
	}

	@Override
	public void storeAll(final Map<Long, BsonPersonProfile> map) {

		LOG.info("\n\n\nstoreAll::" + map);

		map.forEach(this::store);
	}

	@Override
	public BsonPersonProfile load(final Long key) {

		LOG.info("\n\n\nload::" + key);

		// Carrega do Mongo se não estiver no cache
		return repositoryBsonPersonProfile.findById(key);
	}

	@Override
	public Map<Long, BsonPersonProfile> loadAll(final Collection<Long> keys) {

		final Map<Long, BsonPersonProfile> collect = keys.stream()
				.map(key -> repositoryBsonPersonProfile.findById(key))
				.filter(person -> person != null)
				.collect(Collectors.toMap(p -> p.id, p -> p));

		LOG.info("\n\n\nloadAll::" + keys + "\ncollect::" + collect.toString());

		return collect;
	}

	@Override
	public List<Long> loadAllKeys() {

		LOG.info("\n\n\nloadAllKeys>>");

		return repositoryBsonPersonProfile.streamAll()
				.map(p -> p.id)
				.collect(Collectors.toList());
	}

	@Override
	public void delete(final Long key) {
		repositoryBsonPersonProfile.deleteById(key);
	}

	@Override
	public void deleteAll(final Collection<Long> keys) {
		keys.forEach(this::delete);
	}
}