package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.serializer;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOBsonPerson;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public final class BsonPersonSerializer implements CompactSerializer<BsonPerson> {
	@Override
	public BsonPerson read(final CompactReader reader) {
		return new BsonPerson(
				new DTOBsonPerson(
						reader.readInt64("id"),
						reader.readString("name"),
						reader.readInt32("age"),
						reader.readInt64("ts_created")));
	}

	@Override
	public void write(final CompactWriter writer, final BsonPerson obj) {
		writer.writeInt64("id", obj.id);
		writer.writeString("name", obj.name);
		writer.writeInt32("age", obj.age);
		writer.writeInt64("ts_created", obj.ts_created);
	}

	@Override
	public Class<BsonPerson> getCompactClass() {
		return BsonPerson.class;
	}

	@Override
	public String getTypeName() {
		return "BsonPerson"; // Unico dentro do cluster
	}
}