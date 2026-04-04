package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.hazelcast.function.FunctionEx;
import com.hazelcast.jet.kafka.KafkaSources;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sink;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.StreamSource;
import com.hazelcast.jet.pipeline.StreamSourceStage;
import com.hazelcast.jet.pipeline.StreamStage;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

public final class ConsumerKafka {

	private final ILogger LOG = Logger.getLogger(ConsumerKafka.class);

	final Properties props = new Properties();
	final StreamSource<String> streamSource;

//	final ToLongFunctionEx<? super Entry<String, String>> timestampFn;

	// projectionFn function to create output objects from the Kafka record. If the projection returns a null for an item, that item will be filtered out.
	private String projectionFn(ConsumerRecord<String, String> consumerRecord) {
		String info = "\nprojectionFn::";
		info += "\nconsumerRecord.key=" + consumerRecord.key() + "\n";
		info += "\nconsumerRecord.offset=" + consumerRecord.offset() + "\n";
		info += "\nconsumerRecord.timestamp=" + consumerRecord.timestamp() + "\n";
		info += "\nconsumerRecord.topic=" + consumerRecord.topic() + "\n";
		info += "\nconsumerRecord.value=" + consumerRecord.value() + "\n";
		info += "\nconsumerRecord.timestampType=" + consumerRecord.timestampType().name() + "\n";

		LOG.info(info);

		return "(...projectionFn...)";
	}

	private String mapFn(final String input) {
		LOG.info("\nMapFn::applyEx::input=" + input);

		return "(...mapFn...)";
	}

	public ConsumerKafka() {
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("key.deserializer", StringDeserializer.class.getCanonicalName());
		props.setProperty("value.deserializer", StringDeserializer.class.getCanonicalName());
		props.setProperty("auto.offset.reset", "earliest");
//		props.setProperty("group.id", "my-jet-group");
		props.setProperty("group.id", UUID.randomUUID().toString());
//		streamSource = KafkaSources.kafka(props, projectionFn, "WebHookCallback");
		streamSource = KafkaSources.kafka(props, (FunctionEx<ConsumerRecord<String, String>, String> & Serializable) consumerRecord -> projectionFn(consumerRecord), "webhookCallback");
	}

	public Pipeline startPipeline() {
		final Pipeline pipeline = Pipeline.create();
		final StreamSourceStage<String> streamSourceStage = pipeline.readFrom(streamSource);		

		StreamStage<String> streamStage = streamSourceStage.withoutTimestamps();

		streamStage = streamStage.map((FunctionEx<String, String> & Serializable) input -> mapFn(input));

		final Sink<String> noop = Sinks.noop();

		streamStage.writeTo(noop);
		return pipeline;
	}

}
