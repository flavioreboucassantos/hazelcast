package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.ConfigLoader;
import com.hazelcast.config.Config;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.mongodb.client.MongoDatabase;

public class MapConfiguratorWaContactProfile implements BaseMapConfigurator {

	private final ILogger LOG = Logger.getLogger(MapConfiguratorWaContactProfile.class);

	final String mapName;

	public MapConfiguratorWaContactProfile() {
		this.mapName = ConfigLoader.getProperty("myApp.hazelcast.WaContactProfile.mapName");
	}

	@Override
	public String getMapName() {
		return mapName;
	}

	@Override
	public void setMapConfig(final MongoDatabase database, final Config config) {

	}

}
