package com.br.flavioreboucassantos.hazelcast_client_quarkus.producer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.flavioreboucassantos.hazelcast_client_quarkus.clientconfigurator.BaseClientConfigurator;
import com.br.flavioreboucassantos.hazelcast_client_quarkus.clientconfigurator.ClientConfiguratorEntityPersonProfile;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class ProducerHazelcastConfig {

	private final Logger LOG = LoggerFactory.getLogger(ProducerHazelcastConfig.class);

	final ClientConfig clientConfig = new ClientConfig();
	final List<BaseClientConfigurator> listBaseClientConfigurator = new ArrayList<BaseClientConfigurator>();

	@Inject
	public ProducerHazelcastConfig(final ClientConfiguratorEntityPersonProfile clientConfiguratorEntityPersonProfile) {
		clientConfig.setClusterName("dev");

		listBaseClientConfigurator.add(clientConfiguratorEntityPersonProfile);
	}

	@Produces
	@ApplicationScoped
	public HazelcastInstance hazelcastInstance() {
		// 1)
		for (int i = 0; i < listBaseClientConfigurator.size(); i++)
			listBaseClientConfigurator.get(i).setClientConfig(clientConfig);

		final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
//		final Config config = client.getConfig();

		// 2)
		/*
		 * Desde o Hazelcast 3.9, adicionar configurações de mapa dinamicamente (via Config.addMapConfig)
		 * exige que todos os elementos da configuração sejam serializáveis,
		 * pois o Hazelcast precisa replicar essa nova configuração para todos os membros do cluster.
		 */
//		for (int i = 0; i < listBaseMapConfig.size(); i++)
//			listBaseMapConfig.get(i).setMapConfig(config);

		return client;
	}

}
