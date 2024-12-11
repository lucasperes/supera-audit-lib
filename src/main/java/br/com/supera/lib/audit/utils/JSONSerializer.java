package br.com.supera.lib.audit.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.supera.lib.audit.domain.entity.jpa.deserialize.ObjectIdDeserializer;
import br.com.supera.lib.audit.domain.entity.jpa.serialize.ObjectIdSerializer;
import br.com.supera.lib.audit.exception.JSONSerializerException;
import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * Classe Utilitaria para manipular JSONs
 */
@UtilityClass
public class JSONSerializer {
	
	@Getter(value = lombok.AccessLevel.PROTECTED)
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private static boolean isInitializable = false;
	
	static {
		init();
	}
	
	private static void init() {
		if(!isInitializable) {
			OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
			OBJECT_MAPPER.registerModule(new JavaTimeModule());
			
			// Registers modules customs
	        SimpleModule objectIdModule = new SimpleModule();
	        objectIdModule.addSerializer(ObjectId.class, new ObjectIdSerializer());
	        objectIdModule.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
	        OBJECT_MAPPER.registerModule(objectIdModule);
			
			OBJECT_MAPPER.findAndRegisterModules();
			isInitializable = true;
		}
	}

	public static <T> T readValue(String source, Class<T> typeTarget) {
		try {			
			return OBJECT_MAPPER.readValue(source, typeTarget);
		} catch(Exception err) {
			final String msg = "ERROR | Erro on try read values from source = [" + source + "] to target class = " + typeTarget;
			throw new JSONSerializerException(msg, err);
		}
	}
	
	public static <T> List<T> readListValue(String source, Class<T> typeTarget) {
		try {
			CollectionType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, typeTarget);
			return OBJECT_MAPPER.readValue(source, listType);
		} catch(Exception err) {
			final String msg = "ERROR | Erro on try read values from source = [" + source + "] to list of target class = " + typeTarget;
			throw new JSONSerializerException(msg, err);
		}
	}
	
	public static String writeValue(Object source) {
		try {			
			return OBJECT_MAPPER.writeValueAsString(source);
		} catch(Exception err) {
			final String msg = "ERROR | Erro on try write values from source class = [" + source.getClass() + "]";
			throw new JSONSerializerException(msg, err);
		}
	}
	
}
