package br.com.supera.lib.audit.domain.entity.jpa.serialize;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializer Customizado para {@link ObjectId}
 */
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

	@Override
	public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.toHexString());
	}

}
