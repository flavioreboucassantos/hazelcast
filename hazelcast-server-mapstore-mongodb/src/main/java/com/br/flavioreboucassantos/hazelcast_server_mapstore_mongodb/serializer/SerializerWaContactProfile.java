package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.serializer;

import com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.entity.EntityWaContactProfile;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class SerializerWaContactProfile implements CompactSerializer<EntityWaContactProfile> {

	@Override
	public EntityWaContactProfile read(final CompactReader reader) {
		return new EntityWaContactProfile(
				reader.readInt64("id"),
				reader.readString("name"));
	}

	@Override
	public void write(final CompactWriter writer, final EntityWaContactProfile obj) {
		writer.writeInt64("id", obj.id);
		writer.writeString("name", obj.name);
	}

	@Override
	public Class<EntityWaContactProfile> getCompactClass() {
		return EntityWaContactProfile.class;
	}

	@Override
	public String getTypeName() {
		return "EntityWaContactProfile";
	}

}
