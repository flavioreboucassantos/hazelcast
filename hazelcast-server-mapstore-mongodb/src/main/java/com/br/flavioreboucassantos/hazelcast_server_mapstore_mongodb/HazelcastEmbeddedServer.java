package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.bsonentity.BsonPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator.MapConfiguratorBsonPersonProfile;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class HazelcastEmbeddedServer {

	static String mapNamePersonProfile;

	public HazelcastEmbeddedServer() {
		final ConfigLoader configLoader = new ConfigLoader();
		mapNamePersonProfile = configLoader.getProperty("myApp.hazelcast.PersonProfile.mapName");
	}

	public static void main(String[] args) throws InterruptedException {
		new HazelcastEmbeddedServer();

		/*
		 * MongoDB
		 */
		// 1. Create a provider that automatically generates codecs for POJOs
		final CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();

		// 2. Combine it with the default codecs (for Strings, Integers, etc.)
		final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(pojoCodecProvider));

		final ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
		// 2. Build the settings object
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.applicationName("MyJavaApp")
				.codecRegistry(pojoCodecRegistry)// Apply the registry to your MongoClientSettings
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		final MongoDatabase database = mongoClient.getDatabase("db_hzc_mapstore");

		/*
		 * Hazelcast MapConfig
		 */
		final Config config = new Config();
		config.getJetConfig().setEnabled(true);

		final MapConfiguratorBsonPersonProfile mapConfiguratorBsonPersonProfile = new MapConfiguratorBsonPersonProfile(database, mapNamePersonProfile);
		mapConfiguratorBsonPersonProfile.setMapConfig(config);

		/*
		 * Hazelcast.newHazelcastInstance
		 */
		HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
		System.out.println("Hazelcast Member iniciado.");

		IMap<Long, BsonPersonProfile> map = hz.getMap(mapNamePersonProfile);
		map.put(100L, new BsonPersonProfile(100L, "nameTeste", 50, 1234567890L));

		// Mantém a JVM rodando
		Thread.currentThread().join();
	}
}
