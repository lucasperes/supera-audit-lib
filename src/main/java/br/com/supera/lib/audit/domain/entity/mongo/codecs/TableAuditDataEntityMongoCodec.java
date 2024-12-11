package br.com.supera.lib.audit.domain.entity.mongo.codecs;

import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonObjectId;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriterSettings;

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
		reader.readStartDocument();
		
		// Cria um BsonDocument vazio para armazenar os valores
        BsonDocument document = new BsonDocument();
		
		 // Processa todos os campos dentro do documento
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName(); // Obtém o nome do campo
            document.put(fieldName, readField(reader)); // Lê e armazena o valor do campo
        }
		
		final String json = document.toJson(JsonWriterSettings.builder().build());
		reader.readEndDocument();
		TableAuditDataEntityMongo<T> entity = JSONSerializer.readValue(json, TableAuditDataEntityMongo.class);
		return entity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<TableAuditDataEntityMongo<T>> getEncoderClass() {
		return (Class<TableAuditDataEntityMongo<T>>) (Class<?>) TableAuditDataEntityMongo.class;
	}
	
	private static BsonValue readField(BsonReader reader) {
        BsonType type = reader.getCurrentBsonType();

        switch (type) {
        	case OBJECT_ID:
        		return new BsonObjectId(reader.readObjectId());
            case STRING:
                return new BsonString(reader.readString());
            case INT32:
                return new BsonInt32(reader.readInt32());
            case INT64:
                return new BsonInt64(reader.readInt64());
            case DOUBLE:
                return new BsonDouble(reader.readDouble());
            case BOOLEAN:
                return new BsonBoolean(reader.readBoolean());
            case NULL:
                reader.readNull();
                return BsonNull.VALUE;
            case DOCUMENT:
                return readSubDocument(reader);
            case ARRAY:
                return readArray(reader);
            default:
                reader.skipValue();
                return null;
        }
    }
	
	private static BsonDocument readSubDocument(BsonReader reader) {
        BsonDocument subDocument = new BsonDocument();

        reader.readStartDocument();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            subDocument.put(fieldName, readField(reader));
        }

        reader.readEndDocument();

        return subDocument;
    }

    private static BsonArray readArray(BsonReader reader) {
        BsonArray array = new BsonArray();

        reader.readStartArray();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            array.add(readField(reader));
        }

        reader.readEndArray();

        return array;
    }

}
