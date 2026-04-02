package com.br.flavioreboucassantos.hazelcast_client_quarkus.clientconfigurator;

import com.hazelcast.client.config.ClientConfig;

public interface BaseClientConfigurator {

	public String getMapName();

	/**
	 * <b>Near Cache no ClientConfig (Lado do Cliente)</b><br>
	 * Esta é a forma recomendada em sistemas cliente/servidor para reduzir o tráfego de rede e o custo de deserialização nos clientes.<br>
	 * A configuração do cliente não depende da do servidor.
	 * <br>
	 * <br>
	 * <b>Diferenças e Melhores Práticas</b><br>
	 * <ul>
	 * <li><b>Onde configurar:</b> Se o objetivo é acelerar um aplicativo Java que se conecta ao cluster, use ClientConfig.</li>
	 * <li><b>Nomeação:</b> No cliente, o nome do Near Cache deve corresponder ao nome do mapa no servidor (pode usar wildcards, ex: mapa*).</li>
	 * <li><b>Invalidação:</b> Recomenda-se manter <invalidate-on-change>true</invalidate-on-change> para garantir que o cliente não leia dados antigos (stale data).</li>
	 * <li><b>Formato de Memória:</b> BINARY (padrão) armazena serializado, economizando memória no cliente. OBJECT armazena o objeto, eliminando a deserialização em leituras, o que é mais rápido.</li>
	 * <li><b>Preloader:</b> Clientes podem usar o preloader para salvar o Near Cache em disco e recarregá-lo após uma reinicialização para aquecimento rápido do cache.</li>
	 * </ul>
	 * 
	 * @param clientConfig
	 */
	public void setClientNearCacheConfig(final ClientConfig clientConfig);

}
