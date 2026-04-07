package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka.webhookcallback;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.common.serialization.StringDeserializer;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.kafka.KafkaSources;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sink;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.StreamSource;
import com.hazelcast.jet.pipeline.StreamSourceStage;
import com.hazelcast.jet.pipeline.StreamStage;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

public final class ConsumerKafkaWebhookCallback {

	static private final ILogger LOG = Logger.getLogger(ConsumerKafkaWebhookCallback.class);

//	final ToLongFunctionEx<? super Entry<String, String>> timestampFn;

	static public Pipeline createPipeline(final HazelcastInstance hz) {
		final Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("key.deserializer", StringDeserializer.class.getCanonicalName());
		props.setProperty("value.deserializer", StringDeserializer.class.getName());
		props.setProperty("auto.offset.reset", "earliest");
		props.setProperty("group.id", UUID.randomUUID().toString());
		props.setProperty("enable.auto.commit", "false");
		final StreamSource<String> streamSource = KafkaSources.kafka(props, new ProjectionFnWebhookCallback(hz), "webhookCallback");

		final Pipeline pipeline = Pipeline.create();
		final StreamSourceStage<String> streamSourceStage = pipeline.readFrom(streamSource);

		StreamStage<String> streamStage = streamSourceStage.withoutTimestamps();

		streamStage = streamStage.map(new MapFnWebhookCallback());

		final Sink<String> noop = Sinks.noop();

		streamStage.writeTo(Sinks.logger());

		return pipeline;
	}

}
