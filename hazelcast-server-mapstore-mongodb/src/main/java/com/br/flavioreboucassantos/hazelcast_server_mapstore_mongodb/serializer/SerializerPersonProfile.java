package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityPersonProfile;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public final class SerializerPersonProfile implements CompactSerializer<EntityPersonProfile> {

	private final ILogger LOG = Logger.getLogger(SerializerPersonProfile.class);

	@Override
	public EntityPersonProfile read(final CompactReader reader) {

		LOG.info("SerializerPersonProfile::read::name::" + reader.readString("name") + "\n\n\n");

		return new EntityPersonProfile(
				reader.readInt64("id"),
				reader.readString("name"),
				reader.readInt32("age"),
				reader.readInt64("tsCreated"));
	}

	@Override
	public void write(final CompactWriter writer, final EntityPersonProfile obj) {

		LOG.info("SerializerPersonProfile::write::name::" + obj.name + "\n\n\n");

		writer.writeInt64("id", obj.id);
		writer.writeString("name", obj.name);
		writer.writeInt32("age", obj.age);
		writer.writeInt64("tsCreated", obj.tsCreated);
	}

	@Override
	public Class<EntityPersonProfile> getCompactClass() {
		return EntityPersonProfile.class;
	}

	@Override
	public String getTypeName() {
		return "EntityPersonProfile"; // Returns the unique type name for the class T.
	}
}