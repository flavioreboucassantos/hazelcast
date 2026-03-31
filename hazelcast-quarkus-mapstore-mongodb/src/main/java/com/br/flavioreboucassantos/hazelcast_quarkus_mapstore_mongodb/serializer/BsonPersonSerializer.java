package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.serializer;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;
import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.dto.DTOBsonPerson;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class BsonPersonSerializer implements CompactSerializer<BsonPerson> {
	@Override
	public BsonPerson read(CompactReader reader) {
		String id = reader.readString("id");
		String name = reader.readString("name");
		int age = reader.readInt32("age");
		return new BsonPerson(new DTOBsonPerson(id, name, age));
	}

	@Override
	public void write(CompactWriter writer, BsonPerson obj) {
		writer.writeString("id", obj.id);
		writer.writeString("name", obj.name);
		writer.writeInt32("age", obj.age);
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