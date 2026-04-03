package com.br.flavioreboucassantos.hazelcast_client_quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.hazelcast.core.HazelcastInstance;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@QuarkusMain
public class HazelcastClientQuarkus {

	public static void main(String... args) {

		Quarkus.run(args);
	}

	@Inject
	HazelcastInstance hazelcastInstance;

	@ConfigProperty(name = "myApp.hazelcast.PersonProfile.mapName")
	String mapName;

	void onStart(@Observes StartupEvent ev) {
//		IMap<String, EntityPersonProfile> map = hazelcastInstance.getMap(mapName);
//		map.destroy();
//		map = hazelcastInstance.getMap(mapName);
//		map.put("recreate", new EntityPersonProfile());
//		map.clear();
	}

}