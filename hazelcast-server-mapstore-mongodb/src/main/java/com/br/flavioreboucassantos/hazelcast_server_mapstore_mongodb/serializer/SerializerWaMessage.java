package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaMessage;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public final class SerializerWaMessage implements CompactSerializer<EntityWaMessage> {

	@Override
	public EntityWaMessage read(final CompactReader reader) {
		return new EntityWaMessage(
				reader.readString("id"),
				reader.readString("displayPhoneNumber"),
				reader.readString("from"),
				reader.readString("type"),
				reader.readString("body"),
				reader.readInt64("tsCreated"));
	}

	@Override
	public void write(final CompactWriter writer, final EntityWaMessage obj) {
		writer.writeString("id", obj.id);
		writer.writeString("displayPhoneNumber", obj.displayPhoneNumber);
		writer.writeString("from", obj.from);
		writer.writeString("type", obj.type);
		writer.writeString("body", obj.body);
		writer.writeInt64("tsCreated", obj.tsCreated);
	}

	@Override
	public Class<EntityWaMessage> getCompactClass() {
		return EntityWaMessage.class;
	}

	@Override
	public String getTypeName() {
		return "EntityWaMessage";
	}
}