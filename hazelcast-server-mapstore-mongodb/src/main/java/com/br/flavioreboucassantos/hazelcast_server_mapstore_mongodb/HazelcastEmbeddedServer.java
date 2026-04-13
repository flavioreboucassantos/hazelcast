package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka.webhookcallback.ConsumerKafkaWebhookCallback;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator.MapConfiguratorPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator.MapConfiguratorWaContactProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator.MapConfiguratorWaMessage;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * @author Flávio Rebouças Santos - flavioReboucasSantos@gmail.com
 */
public class HazelcastEmbeddedServer {
	/*
	 * O Hazelcast é uma plataforma de computação distribuída em memória que utiliza uma arquitetura "masterless" (sem mestre), onde os dados são divididos em partições (partitions) e distribuídos entre os nós (nodes/members) do cluster.
	 * Essa estrutura permite escalabilidade horizontal e alta disponibilidade.
	 * 
	 * Aqui está o funcionamento detalhado:
	 * 1. Nodes (Nós/Membros)
	 * - O que são: Cada instância da JVM executando o Hazelcast é considerada um nó ou membro do cluster.
	 * - Arquitetura Masterless: Todos os nós são iguais. Não existe um "nó mestre" central, o que evita gargalos e pontos únicos de falha.
	 * - Responsabilidade: Os nós se comunicam entre si para gerenciar o cluster, replicar dados e lidar com falhas automaticamente. Ao adicionar novos nós, o cluster se rebalanceia sozinho.
	 * 
	 * 2. Partitions (Partições)
	 * - O que são: Partições são os segmentos lógicos de memória nos quais o Hazelcast armazena dados. O Hazelcast divide os dados do cluster nessas partições para processamento paralelo.
	 * - Quantidade padrão: Por padrão, o Hazelcast divide os dados em 271 partições.
	 * - Distribuição: Essas 271 partições são distribuídas uniformemente entre todos os nós disponíveis. Se houver 1 nó, ele detém todas as 271. Se houver 2 nós, cada um detém aproximadamente 135 ou 136.
	 * 
	 * 3. Como funciona a Partição e Distribuição (Hashing)
	 * - O Hazelcast usa um algoritmo de hashing consistente para decidir onde cada dado é armazenado:
	 * - Chave da Entrada: Quando você insere um dado (ex: map.put(key, value)), o Hazelcast serializa a chave (key) em um array de bytes.
	 * - Hashing: Esse array de bytes passa por um algoritmo de hash.
	 * - Módulo (MOD): O resultado do hash é aplicado a um cálculo de módulo: MOD(hash_result, 271).
	 * - ID da Partição: O resultado define o ID da partição (de 0 a 270) onde o par chave-valor será guardado.
	 * 
	 * 4. Backup e Disponibilidade (Replicação)
	 * - Para garantir a segurança dos dados, o Hazelcast cria réplicas (backups) das partições:
	 * - Primary Partitions: A partição "dona" do dado é a primária.
	 * - Backup Partitions: Cópias dessas partições são distribuídas em outros nós.
	 * - Exemplo: Se você configurar 1 backup, cada partição primária tem uma cópia em outro nó. Se um nó cair, o nó que continha o backup assume o papel de primário, evitando perda de dados.
	 * 
	 * Resumo do Funcionamento
	 * - Escalabilidade: Mais nós = mais partições distribuídas = mais memória e processamento paralelo.
	 * - Balanceamento: O Hazelcast tenta manter o número de partições igual em todos os nós para evitar "pontos quentes" (hotspots).
	 * - Partição de Rede (Network Partition): O Hazelcast foi projetado para alta disponibilidade (AP - Availability & Partition Tolerance), mantendo o sistema funcional mesmo com falhas de rede entre os nós, priorizando a consistência final (eventual
	 * consistency).
	 */

	static private final ILogger LOG = Logger.getLogger(HazelcastEmbeddedServer.class);

	/*
	 * MongoDB
	 */
	static private MongoDatabase database;

	static {
		// 1. Create a provider that automatically generates codecs for POJOs
		final CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();

		// 2. Combine it with the default codecs (for Strings, Integers, etc.)
		final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(pojoCodecProvider));

		final ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
		// 3. Build the settings object
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.applicationName("HazelcastEmbeddedServer")
				.codecRegistry(pojoCodecRegistry)// Apply the registry to your MongoClientSettings
				.build();
		MongoClient mongoClient = MongoClients.create(settings);

		database = mongoClient.getDatabase("db_hzc_mapstore");
	}

	/*
	 * Hazelcast MapConfig
	 */
	static private final Config hazelcastConfig = new Config();

	static {
		hazelcastConfig.setLicenseKey(ConfigLoader.getProperty("hazelcast.license-key"));
		hazelcastConfig.getJetConfig().setEnabled(true);
		hazelcastConfig.getJetConfig().setResourceUploadEnabled(true);

		new MapConfiguratorPersonProfile().setMapConfig(database, hazelcastConfig); // <----------
		new MapConfiguratorWaContactProfile().setMapConfig(database, hazelcastConfig); // <----------
		new MapConfiguratorWaMessage().setMapConfig(database, hazelcastConfig); // <----------
	}

	/*
	 * Hazelcast.newHazelcastInstance
	 */
	static public final HazelcastInstance hz = Hazelcast.newHazelcastInstance(hazelcastConfig);

	/*
	 * Hazelcast Jet Jobs
	 */
	static {
		System.out.println("Hazelcast Member iniciado.");

		final Pipeline pipelineConsumerKafkaWebhookCallback = ConsumerKafkaWebhookCallback.createPipeline(hz);
		final JobConfig jobConfigConsumerKafkaWebhookCallback = new JobConfig();
		/*
		 * Define quais garantias de processamento de mensagens são oferecidas em caso de falha.
		 * Especificamente, se um membro do cluster sair do cluster durante a execução de uma tarefa e a tarefa for reiniciada automaticamente, define a semântica de em que ponto do fluxo a tarefa será retomada.
		 * 
		 * Quando AT_LEAST_ONCE ou EXACTLY_ONCE estiver definido, o snapshot distribuído será habilitado para a tarefa.
		 * O algoritmo de snapshot distribuído funciona enviando barreiras pelo fluxo, que, ao serem recebidas, fazem com que os processadores salvem seu estado como um snapshot.
		 * Os snapshots são salvos na memória e replicados em todo o cluster.
		 * 
		 * Como um processador pode ter múltiplas entradas, ele precisa esperar até que a barreira seja recebida de todas as entradas antes de capturar um instantâneo.
		 * A diferença entre AT_LEAST_ONCE e EXACTLY_ONCE é que, no modo AT_LEAST_ONCE, o processador pode continuar processando itens de entradas que já receberam a barreira.
		 * Isso resulta em menor latência e maior taxa de transferência geral, com a ressalva de que alguns itens podem ser processados ​​duas vezes após uma reinicialização.
		 */
		jobConfigConsumerKafkaWebhookCallback.setProcessingGuarantee(ProcessingGuarantee.EXACTLY_ONCE);
		/*
		 * Define o intervalo de captura de instantâneo em milissegundos — o intervalo entre a conclusão do instantâneo anterior e o início de um novo.
		 * Deve ser definido com um valor positivo.
		 * Essa configuração só é relevante com garantias de processamento "pelo menos uma vez" ou "exatamente uma vez".
		 * 
		 * O valor padrão é definido como 10 segundos.
		 */
		jobConfigConsumerKafkaWebhookCallback.setSnapshotIntervalMillis(12000);
//		jobConfigConsumerKafka.addClass(ConsumerKafka.class);
		hz.getJet().newJob(pipelineConsumerKafkaWebhookCallback, jobConfigConsumerKafkaWebhookCallback);
	}

	public static void main(String[] args) throws InterruptedException {
		// Mantém a JVM rodando
		Thread.currentThread().join();
	}
}
