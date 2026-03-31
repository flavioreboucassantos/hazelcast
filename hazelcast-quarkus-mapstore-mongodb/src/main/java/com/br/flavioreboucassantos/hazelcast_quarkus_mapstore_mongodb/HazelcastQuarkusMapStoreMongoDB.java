package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class HazelcastQuarkusMapStoreMongoDB {
	
	public static void main(String... args) {
		
		Quarkus.run(args);
	}
	
}