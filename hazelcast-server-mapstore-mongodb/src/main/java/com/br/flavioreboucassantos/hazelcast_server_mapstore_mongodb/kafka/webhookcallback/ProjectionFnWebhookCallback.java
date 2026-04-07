package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka.webhookcallback;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.ConfigLoader;
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

public class ProjectionFnWebhookCallback implements FunctionEx<ConsumerRecord<String, String>, String> {

	/*
	 * projectionFn:
	 * Function to create output objects from the Kafka record.
	 * If the projection returns a null for an item, that item will be filtered out.
	 */

	static private final ILogger LOG = Logger.getLogger(ProjectionFnWebhookCallback.class);

	static private final ObjectMapper objectMapper = new ObjectMapper();

	static private HazelcastInstance hz;
	static private IMap<Object, Object> mapWaContactProfile;
	static private IMap<Object, Object> mapWaMessage;

	public ProjectionFnWebhookCallback(final HazelcastInstance hz) {
		this.hz = hz;
		mapWaContactProfile = hz.getMap(ConfigLoader.getProperty("myApp.hazelcast.WaContactProfile.mapName"));
		mapWaMessage = hz.getMap(ConfigLoader.getProperty("myApp.hazelcast.WaMessage.mapName"));
	}

	private void put(final EntityWaContactProfile entityWaContactProfile) {

	}

	private void put(final EntityWaMessage entityWaMessage) {

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
