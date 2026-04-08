package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka.webhookcallback;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.ConfigLoader;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.HazelcastEmbeddedServer;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaContactProfile;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaMessage;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback.JSONWebHookCallback;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback.JSONWebHookCallbackEntryChanges;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback.JSONWebHookCallbackEntryChangesValue;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback.JSONWebHookCallbackEntryChangesValueContacts;
import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.jsonclass.webhookcallback.JSONWebHookCallbackEntryChangesValueMessages;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.function.FunctionEx;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.map.IMap;

import tools.jackson.databind.ObjectMapper;

/**
 * projectionFn:<br>
 * Function to create output objects from the Kafka record.<br>
 * If the projection returns a null for an item, that item will be filtered out.
 */
public final class ProjectionFnWebhookCallback implements FunctionEx<ConsumerRecord<String, String>, String> {

	static private final long serialVersionUID = 8088628284213949111L;

	static private final ILogger LOG = Logger.getLogger(ProjectionFnWebhookCallback.class);

	static private final ObjectMapper objectMapper = new ObjectMapper();

	static private final HazelcastInstance hz = HazelcastEmbeddedServer.hz;
	static private final IMap<Object, Object> mapWaContactProfile = hz.getMap(ConfigLoader.getProperty("mapName.WaContactProfile"));
	static private final IMap<Object, Object> mapWaMessage = hz.getMap(ConfigLoader.getProperty("mapName.WaMessage"));

	private void put(final EntityWaContactProfile entityWaContactProfile) {
		mapWaContactProfile.put(entityWaContactProfile.id, entityWaContactProfile);
	}

	private void put(final EntityWaMessage entityWaMessage) {
		mapWaMessage.putIfAbsent(entityWaMessage.id, entityWaMessage);
	}

	@Override
	public String applyEx(final ConsumerRecord<String, String> consumerRecord) throws Exception {
//		String info = "\nprojectionFn::";
//		info += "\nconsumerRecord.key=" + consumerRecord.key() + "\n";
//		info += "\nconsumerRecord.offset=" + consumerRecord.offset() + "\n";
//		info += "\nconsumerRecord.timestamp=" + consumerRecord.timestamp() + "\n";
//		info += "\nconsumerRecord.topic=" + consumerRecord.topic() + "\n";
//		info += "\nconsumerRecord.value=" + consumerRecord.value() + "\n";
//		info += "\nconsumerRecord.timestampType=" + consumerRecord.timestampType().name() + "\n";
//		LOG.info(info);

		final long tsCreated = consumerRecord.timestamp();

		final JSONWebHookCallback jsonWebHookCallback = objectMapper.readValue(consumerRecord.value(), JSONWebHookCallback.class);

		final JSONWebHookCallbackEntryChanges changesFirst = jsonWebHookCallback.entry().getFirst().changes().getFirst();
		final String field = changesFirst.field();

		final JSONWebHookCallbackEntryChangesValue value = changesFirst.value();
		final String messagingProduct = value.messagingProduct();
		final String displayPhoneNumber = value.metadata().displayPhoneNumber();
		final JSONWebHookCallbackEntryChangesValueContacts firstContact = value.contacts().getFirst();

		final String firstContactWaId = firstContact.waId();
		final String firstContacProfileName = firstContact.profile().name();

		put(new EntityWaContactProfile(Long.parseLong(firstContactWaId), firstContacProfileName));

		switch (field) {
		case "messages":
			final JSONWebHookCallbackEntryChangesValueMessages firstMessage = value.messages().getFirst();
			final String fromMessage = firstMessage.from();
			final String idMessage = firstMessage.id();
			final String typeMessage = firstMessage.type();
			final String body = firstMessage.text().body();

			put(new EntityWaMessage(idMessage, displayPhoneNumber, fromMessage, typeMessage, body, tsCreated));

			break;
		case "statuses":
			break;
		case "reaction":
			break;
		default:
			break;
		}

		return "(...projectionFn...)";
	}

}
