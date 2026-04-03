package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer;

import com.br.flavioreboucassantos.hazelcast_client_quarkus.entity.EntityPersonProfile;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public final class SerializerEntityPersonProfile implements CompactSerializer<EntityPersonProfile> {

	private final ILogger LOG = Logger.getLogger(SerializerEntityPersonProfile.class);

	final String mapName;

	public SerializerEntityPersonProfile(final String mapName) {
		this.mapName = mapName;
	}

	@Override
	public EntityPersonProfile read(final CompactReader reader) {

		LOG.info("\n\n\nSerializerEntityPersonProfile::read::name" + reader.readString("name"));

		return new EntityPersonProfile(
				reader.readInt64("id"),
				reader.readString("name"),
				reader.readInt32("age"),
				reader.readInt64("tsCreated"));
	}

	@Override
	public void write(final CompactWriter writer, final EntityPersonProfile obj) {
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
		return mapName; // Unico dentro do cluster
	}
}