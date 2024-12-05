package br.com.supera.lib.audit.config;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.supera.lib.audit.annotation.TableProperties;
import br.com.supera.lib.audit.domain.entity.mongo.AbstractAuditEntityMongo;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import br.com.supera.lib.audit.domain.model.database.filters.BaseFiltersDatabaseModel;
import br.com.supera.lib.audit.exception.DatabaseException;
import br.com.supera.lib.audit.exception.IllegalArgumentException;
import lombok.Getter;

public class MongoConnectionFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoConnectionFactory.class);
	
	private static final String STRING_CONNECTION = "mongodb+srv://%s:%s@%s";
	
	private DatabaseConnectionConfigModel config;
	
	@Getter
	private static MongoClient client;
	
	public MongoConnectionFactory(DatabaseConnectionConfigModel config) {
		this.config = config;
		buildClient();
	}
	
	private void buildClient() {
		final String url = createURLConnection();
		client = MongoClients.create(url);
	}
	
	private String createURLConnection() {
		return String.format(STRING_CONNECTION, config.getUsername(), config.getPassword(), config.getHost());
	}
	
	public <T extends AbstractAuditEntityMongo<?>> void insert(T entity, Class<T> type) {
		try {
			if(entity == null) {
				throw new IllegalArgumentException("Entity is null");
			}
			if(!entity.getClass().isAnnotationPresent(TableProperties.class)) {
				throw new IllegalArgumentException("Entity not have @TableProperties annotation declared");
			}
			MongoDatabase database = client.getDatabase(config.getDatabase());
			
			TableProperties props = entity.getClass().getAnnotation(TableProperties.class);
			MongoCollection<T> collection = database.getCollection(props.collectionName(), type);
			final var result = collection.insertOne(entity);
			LOGGER.info("INFO | Sucess on insert entity with returned ID = {}", result.getInsertedId().asString().getValue());
		} catch(Exception err) {
			throw new DatabaseException("ERROR | Error on try persist entity on mongo database", err);
		}
	}
	
	public <T extends AbstractAuditEntityMongo<?>> List<T> list(BaseFiltersDatabaseModel filters, Class<T> type) {
		try {
			if(type == null) {
				throw new IllegalArgumentException("Type result is null");
			}
			if(!type.isAnnotationPresent(TableProperties.class)) {
				throw new IllegalArgumentException("Type result not have @TableProperties annotation declared");
			}
			MongoDatabase database = client.getDatabase(config.getDatabase());
			
			TableProperties props = type.getAnnotation(TableProperties.class);
			MongoCollection<T> collection = database.getCollection(props.collectionName(), type);
			final var filtersBson = createFiltersBson();
			final var result = collection.find(filtersBson, type)
					.skip(0)
					.limit(0);
			
			List<T> response = new ArrayList<>();
			
			
			LOGGER.info("INFO | Sucess on list entities with returned result occurs = {}", response.size());
			
			return response;
		} catch(Exception err) {
			throw new DatabaseException("ERROR | Error on try find entities on mongo database", err);
		}
	}
	
	private Bson createFiltersBson() {
		return null;
	}

}
