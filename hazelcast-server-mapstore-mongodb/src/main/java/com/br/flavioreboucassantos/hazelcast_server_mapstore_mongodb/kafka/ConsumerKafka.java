package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka;

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

	static private final ILogger LOG = Logger.getLogger(ConsumerKafka.class);

	/*
	 * projectionFn:
	 * Function to create output objects from the Kafka record.
	 * If the projection returns a null for an item, that item will be filtered out.
	 */
	static private FunctionEx<ConsumerRecord<String, String>, String> projectionFn = (final ConsumerRecord<String, String> consumerRecord) -> {
		String info = "\nprojectionFn::";
		info += "\nconsumerRecord.key=" + consumerRecord.key() + "\n";
		info += "\nconsumerRecord.offset=" + consumerRecord.offset() + "\n";
		info += "\nconsumerRecord.timestamp=" + consumerRecord.timestamp() + "\n";
		info += "\nconsumerRecord.topic=" + consumerRecord.topic() + "\n";
		info += "\nconsumerRecord.value=" + consumerRecord.value() + "\n";
		info += "\nconsumerRecord.timestampType=" + consumerRecord.timestampType().name() + "\n";

		LOG.info(info);

		return "(...projectionFn...)";
	};

//	final ToLongFunctionEx<? super Entry<String, String>> timestampFn;

	/*
	 * mapFn:
	 * A mapping stage which applies the given function to each input item independently and emits the function's result as the output item.
	 * If the result is null , it emits nothing.
	 * Therefore, this stage can be used to implement filtering semantics as well.
	 */
	static private FunctionEx<? super String, ? extends String> mapFn = (final String input) -> {
		LOG.info("\nMapFn::applyEx::input=" + input);

		return "(...mapFn...)";
	};

	static public Pipeline createPipeline() {
		final Properties props = new Properties();
		props.setProperty("bootstrap.servers", "localhost:9092");
		props.setProperty("key.deserializer", StringDeserializer.class.getCanonicalName());
		props.setProperty("value.deserializer", StringDeserializer.class.getCanonicalName());
		props.setProperty("auto.offset.reset", "earliest");
		props.setProperty("group.id", UUID.randomUUID().toString());
		props.setProperty("enable.auto.commit", "false");
		final StreamSource<String> streamSource = KafkaSources.kafka(props, projectionFn, "webhookCallback");

		final Pipeline pipeline = Pipeline.create();
		final StreamSourceStage<String> streamSourceStage = pipeline.readFrom(streamSource);

		StreamStage<String> streamStage = streamSourceStage.withoutTimestamps();

		streamStage = streamStage.map(mapFn);

		final Sink<String> noop = Sinks.noop();

		streamStage.writeTo(Sinks.logger());

		return pipeline;
	}

}
