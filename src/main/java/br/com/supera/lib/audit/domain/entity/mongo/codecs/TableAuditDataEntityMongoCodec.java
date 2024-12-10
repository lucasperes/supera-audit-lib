package br.com.supera.lib.audit.domain.entity.mongo.codecs;

import org.bson.BsonDocument;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import br.com.supera.lib.audit.domain.entity.jpa.AbstractAuditEntity;
import br.com.supera.lib.audit.domain.entity.mongo.TableAuditDataEntityMongo;
import br.com.supera.lib.audit.utils.JSONSerializer;

/**
 * Classe Codec para o Mongo Conseguir Serialziar esses Objetos Genericos
 * 
 * @param <T> Tipo da Entidade a ser auditada
 */
public class TableAuditDataEntityMongoCodec<T extends AbstractAuditEntity<?>>
		implements Codec<TableAuditDataEntityMongo<T>> {

	@Override
	public void encode(BsonWriter writer, TableAuditDataEntityMongo<T> value, EncoderContext encoderContext) {
		String json = JSONSerializer.writeValue(value);
		BsonDocument doc = BsonDocument.parse(json);
		BsonDocumentCodec codec = new BsonDocumentCodec();
	    codec.encode(writer, doc, encoderContext);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TableAuditDataEntityMongo<T> decode(BsonReader reader, DecoderContext decoderContext) {
		final String json = reader.readString();
		TableAuditDataEntityMongo<T> entity = JSONSerializer.readValue(json, TableAuditDataEntityMongo.class);
		return entity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<TableAuditDataEntityMongo<T>> getEncoderClass() {
		return (Class<TableAuditDataEntityMongo<T>>) (Class<?>) TableAuditDataEntityMongo.class;
	}

}
