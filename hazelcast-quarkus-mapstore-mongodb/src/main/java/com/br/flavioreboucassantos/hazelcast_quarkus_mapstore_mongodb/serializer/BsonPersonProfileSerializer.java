package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.serializer;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bsonentity.BsonPersonProfile;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public final class BsonPersonProfileSerializer implements CompactSerializer<BsonPersonProfile> {

	@Override
	public BsonPersonProfile read(final CompactReader reader) {
		return new BsonPersonProfile(
				reader.readInt64("id"),
				reader.readString("name"),
				reader.readInt32("age"),
				reader.readInt64("tsCreated"));
	}

	@Override
	public void write(final CompactWriter writer, final BsonPersonProfile obj) {
		writer.writeInt64("id", obj.id);
		writer.writeString("name", obj.name);
		writer.writeInt32("age", obj.age);
		writer.writeInt64("tsCreated", obj.tsCreated);
	}

	@Override
	public Class<BsonPersonProfile> getCompactClass() {
		return BsonPersonProfile.class;
	}

	@Override
	public String getTypeName() {
		return "BsonPersonProfile"; // Unico dentro do cluster
	}
}