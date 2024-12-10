package br.com.supera.lib.audit.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import br.com.supera.lib.audit.annotation.TableProperties;
import br.com.supera.lib.audit.domain.entity.mongo.AbstractAuditEntityMongo;
import br.com.supera.lib.audit.domain.entity.mongo.codecs.TableAuditDataEntityMongoCodec;
import br.com.supera.lib.audit.domain.enums.database.OperatorsDatabaseEnum;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import br.com.supera.lib.audit.domain.model.database.filters.BaseFiltersDatabaseModel;
import br.com.supera.lib.audit.domain.model.database.filters.ValueOperatorModel;
import br.com.supera.lib.audit.domain.model.database.filters.ValueSorterModel;
import br.com.supera.lib.audit.domain.model.database.request.PaginationRequestModel;
import br.com.supera.lib.audit.domain.model.database.response.ResultPaginatedModel;
import br.com.supera.lib.audit.exception.DatabaseException;
import br.com.supera.lib.audit.exception.IllegalArgumentException;
import lombok.Getter;

public class MongoConnectionFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoConnectionFactory.class);
	
	private static final String STRING_CONNECTION = "mongodb://%s:%s";
	private static final String STRING_CONNECTION_USER_PROVIDER = "mongodb://%s:%s@%s:%s";
	
	private DatabaseConnectionConfigModel config;
	
	@Getter
	private static MongoClient client;
	
	public MongoConnectionFactory(DatabaseConnectionConfigModel config) {
		this.config = config;
		buildClient();
	}
	
	private void buildClient() {
		CodecRegistry customCodecRegistry = CodecRegistries.fromCodecs(new TableAuditDataEntityMongoCodec<>());
		
		CodecRegistry combinedCodecRegistry = CodecRegistries.fromRegistries(
				customCodecRegistry,
				MongoClientSettings.getDefaultCodecRegistry());
		
		final String url = createURLConnection();
		
		MongoClientSettings settings = MongoClientSettings.builder()
		        .codecRegistry(combinedCodecRegistry)
		        .applyConnectionString(new ConnectionString(url))
		        .build();
		
		client = MongoClients.create(settings);
	}
	
	private String createURLConnection() {
		String url = String.format(STRING_CONNECTION, config.getHost(), config.getPort());
		if(config.getUsername() != null && !config.getUsername().isBlank()
				&& config.getPassword() != null && !config.getPassword().isBlank()) {
			url = String.format(STRING_CONNECTION_USER_PROVIDER, config.getUsername(), config.getPassword(), config.getHost(), config.getPort());
		}
		return url;
	}
	
	public <T extends AbstractAuditEntityMongo> T insert(T entity, Class<T> type) {
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
			final var idInserted = result.getInsertedId().asObjectId().getValue();
			entity.setId(idInserted);
			LOGGER.info("INFO | Sucess on insert entity with returned ID = {}", idInserted);
			
			return entity;
		} catch(Exception err) {
			throw new DatabaseException("ERROR | Error on try persist entity on mongo database", err);
		}
	}
	
	public <T extends AbstractAuditEntityMongo> ResultPaginatedModel<T> list(BaseFiltersDatabaseModel filters, Class<T> type) {
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
			final var filtersBson = createFiltersBson(filters);
			final var sortsBson = createSortersBson(filters);
			
			// Calculate the skip
			PaginationRequestModel pagination = filters.getPagination();
			final var skip = pagination.getPage() * pagination.getSize();
			
			List<T> response = collection.find(filtersBson, type)
					.skip(skip)
					.limit(pagination.getSize())
					.sort(sortsBson)
					.into(new ArrayList<>());
			
			final var count = collection.countDocuments(filtersBson);
			final var totalPages = count / pagination.getSize();
			
			LOGGER.info("INFO | Sucess on list entities with returned result occurs = {}", response.size());
			
			return ResultPaginatedModel.<T>builder()
					.page(pagination.getPage())
					.size(pagination.getSize())
					.totalPages((int) totalPages)
					.totalElements((int) count)
					.content(response)
					.build();
		} catch(Exception err) {
			throw new DatabaseException("ERROR | Error on try find entities on mongo database", err);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Bson createFiltersBson(BaseFiltersDatabaseModel filters) {
		Map<String, Object> params = filters.buildFilters();
		if(params != null && !params.isEmpty()) {
			List<Bson> listOperators = new ArrayList<>();
			for(Entry<String, Object> entry : params.entrySet()) {
				final var key = entry.getKey();
				final var value = entry.getValue();
				// Valor unico
				if(value instanceof ValueOperatorModel) {
					ValueOperatorModel operator = (ValueOperatorModel) value;
					Bson bson = extractOperatorValue(key, operator);					
					listOperators.add(bson);
				}
				// Lista de valores
				else if(value instanceof List) {
					List<ValueOperatorModel> operators = (List<ValueOperatorModel>) value;
					for(ValueOperatorModel operator : operators) {
						Bson bson = extractOperatorValue(key, operator);
						listOperators.add(bson);
					}
				}
			}
			
			if(!listOperators.isEmpty()) {
				if(OperatorsDatabaseEnum.AND.equals(filters.getOperator())) {
					return Filters.and(listOperators);
				} else if(OperatorsDatabaseEnum.OR.equals(filters.getOperator())) {
					return Filters.or(listOperators);
				}
			}
		}
		return null;
	}
	
	private Bson createSortersBson(BaseFiltersDatabaseModel filters) {
		if(filters.getSorters() != null && !filters.getSorters().isEmpty()) {
			List<Bson> listSorters = new ArrayList<>();
			for(ValueSorterModel sort : filters.getSorters()) {
				Bson bson = extractSorterValue(sort);
				listSorters.add(bson);
			}
			
			if(!listSorters.isEmpty()) {
				return Sorts.orderBy(listSorters);
			}
		}
		return null;
	}
	
	private Bson extractSorterValue(ValueSorterModel sort) {
		switch(sort.getType()) {
			case ASC:
				return Sorts.ascending(sort.getNameAttr());
			case DESC:
				return Sorts.descending(sort.getNameAttr());
			default:
				throw new IllegalArgumentException("Type sorter query database is invalid");
		}
	}
	
	private Bson extractOperatorValue(String nameAttr, ValueOperatorModel value) {
		switch(value.getOperator()) {
			case EQ:
				return Filters.eq(nameAttr, value.getValue());
			case GT:
				return Filters.gt(nameAttr, value.getValue());
			case GTE:
				return Filters.gte(nameAttr, value.getValue());
			case LT:
				return Filters.lt(nameAttr, value.getValue());
			case LTE:
				return Filters.lte(nameAttr, value.getValue());
			default:
				throw new IllegalArgumentException("Type operator database is invalid");
		}
	}

}
