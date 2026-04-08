package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator;

import com.hazelcast.config.Config;
import com.mongodb.client.MongoDatabase;

public interface BaseMapConfigurator {

	public String getMapName();

	public void setMapConfig(final MongoDatabase database, final Config config);

}
