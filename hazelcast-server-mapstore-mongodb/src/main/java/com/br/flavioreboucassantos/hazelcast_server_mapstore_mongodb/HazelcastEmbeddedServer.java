package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka.ConsumerKafka;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapconfigurator.MapConfiguratorPersonProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.mapstore.MapStorePersonProfile;
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

public class HazelcastEmbeddedServer {

	static private final ILogger LOG = Logger.getLogger(MapStorePersonProfile.class);

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
				.applicationName("HazelcastEmbeddedServer")
				.codecRegistry(pojoCodecRegistry)// Apply the registry to your MongoClientSettings
				.build();
		MongoClient mongoClient = MongoClients.create(settings);
		final MongoDatabase database = mongoClient.getDatabase("db_hzc_mapstore");

		/*
		 * Hazelcast MapConfig
		 */
		final Config config = new Config();
		config.getJetConfig().setEnabled(true);
		config.getJetConfig().setResourceUploadEnabled(true);

		final MapConfiguratorPersonProfile mapConfiguratorPersonProfile = new MapConfiguratorPersonProfile(database, mapNamePersonProfile);
		mapConfiguratorPersonProfile.setMapConfig(config);

		/*
		 * Hazelcast.newHazelcastInstance
		 */
		HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
		System.out.println("Hazelcast Member iniciado.");

		final Pipeline pipeline = ConsumerKafka.createPipeline();
		final JobConfig jobConfig = new JobConfig();
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
		jobConfig.setProcessingGuarantee(ProcessingGuarantee.EXACTLY_ONCE);
		/*
		 * Define o intervalo de captura de instantâneo em milissegundos — o intervalo entre a conclusão do instantâneo anterior e o início de um novo.
		 * Deve ser definido com um valor positivo.
		 * Essa configuração só é relevante com garantias de processamento "pelo menos uma vez" ou "exatamente uma vez".
		 * 
		 * O valor padrão é definido como 10 segundos.
		 */
		jobConfig.setSnapshotIntervalMillis(12000);
//		jobConfig.addClass(ConsumerKafka.class);
		hz.getJet().newJob(pipeline, jobConfig);

		// Mantém a JVM rodando
		Thread.currentThread().join();
	}
}
