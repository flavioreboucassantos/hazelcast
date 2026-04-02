package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator;

import com.hazelcast.config.Config;

public interface BaseMapConfigurator {

	public String getMapName();

	public void setMapConfig(final Config config);

}
