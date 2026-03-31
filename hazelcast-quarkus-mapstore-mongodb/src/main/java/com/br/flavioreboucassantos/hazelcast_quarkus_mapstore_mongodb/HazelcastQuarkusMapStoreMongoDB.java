package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@QuarkusMain
public class HazelcastQuarkusMapStoreMongoDB {

	public static void main(String... args) {

		Quarkus.run(args);
	}

	@Inject
	HazelcastInstance hazelcastInstance;

	void onStart(@Observes StartupEvent ev) {
//		IMap<Object, Object> map = hazelcastInstance.getMap("bsonPerson");
//		map.destroy();
	}

}