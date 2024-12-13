package br.com.supera.lib.audit.config.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.supera.lib.audit.config.IDatabaseConnectionFactory;
import br.com.supera.lib.audit.domain.entity.EntityBase;
import br.com.supera.lib.audit.domain.enums.database.jpa.JpaProviderDatabaseEnum;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import br.com.supera.lib.audit.domain.model.database.filters.BaseFiltersDatabaseModel;
import br.com.supera.lib.audit.domain.model.database.response.ResultPaginatedModel;
import br.com.supera.lib.audit.exception.DatabaseException;
import br.com.supera.lib.audit.exception.IllegalArgumentException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe Factory para fabrica de conexoes com banco atraves de JPA
 */
public class JpaConnectionFactoryImpl implements IDatabaseConnectionFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaConnectionFactoryImpl.class);
	
	private static final String PERSISTENCE_UNIT_NAME = "persistenceAudit";
	
	private DatabaseConnectionConfigModel config;
	
	private static EntityManagerFactory factory;
	
	public JpaConnectionFactoryImpl(DatabaseConnectionConfigModel config) {
		this.config = config;
		buildClient();
	}

	@Override
	public void buildClient() {
        if(config.getProviderJpa() == null) {
        	throw new IllegalArgumentException("Provider JPA is null");
        }
        
        JpaProviderDatabaseEnum provider = config.getProviderJpa();
		
        final var url = createURLConnection();
        
        Map<String, Object> settings = new HashMap<>();
        settings.put("hibernate.dialect", provider.getDialect());
        settings.put("hibernate.hbm2ddl.auto", provider.getHbm2ddl());
        settings.put("hibernate.show_sql", "false");
        settings.put("javax.persistence.jdbc.driver", provider.getDriver());
        settings.put("javax.persistence.jdbc.url", url);
        settings.put("javax.persistence.jdbc.user", config.getUsername());
        settings.put("javax.persistence.jdbc.password", config.getPassword());

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, settings);
	}

	@Override
	public <T extends EntityBase> T insert(T entity, Class<T> type) {
		EntityManager manager = factory.createEntityManager();
		try {
			manager.getTransaction().begin();
			manager.persist(entity);
			manager.getTransaction().commit();
			return entity;
		} catch(Exception err) {
			LOGGER.error("ERROR | Error on try commit transaction JPA {}", err);
			manager.getTransaction().rollback();
		} finally {
			manager.close();
		}
		throw new DatabaseException("Erro on persist entity by EntityManager JPA");
	}

	@Override
	public <T extends EntityBase> ResultPaginatedModel<T> list(BaseFiltersDatabaseModel filters, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public EntityManager createManager() {
		return factory.createEntityManager();
	}
	
	public void closeFactory() {
		if(factory != null && factory.isOpen()) {
			factory.close();
		}
	}
	
	private String createURLConnection() {
		switch(config.getProviderJpa()) {
			case H2:
				return config.getProviderJpa().getUrl();
			case POSTGRES:
				return String.format(
						config.getProviderJpa().getUrl(),
						config.getHost(),
						config.getPort(),
						config.getDatabase());
			default:
				throw new IllegalArgumentException("Provider JPA is invalid");
		}
	}

}
