package br.gov.rs.lib.audit.service;

import br.gov.rs.lib.audit.config.IDatabaseConnectionFactory;
import br.gov.rs.lib.audit.config.impl.JpaConnectionFactoryImpl;
import br.gov.rs.lib.audit.config.impl.MongoConnectionFactoryImpl;
import br.gov.rs.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import br.gov.rs.lib.audit.exception.IllegalArgumentException;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Classe Service base para ser extendida pelas classes Services da LIB
 */
public abstract class AbstractAuditServiceBase {

	@Getter(value = AccessLevel.PROTECTED)
	private IDatabaseConnectionFactory connection;
	
	public AbstractAuditServiceBase(DatabaseConnectionConfigModel config) {
		if(config.getProvider() == null) {
			throw new IllegalArgumentException("Provider database is null");
		}
		switch(config.getProvider()) {
			case MONGO_DB:
				connection = new MongoConnectionFactoryImpl(config);
				break;
			case JPA:
				connection = new JpaConnectionFactoryImpl(config);
				break;
			default:
				throw new IllegalArgumentException("Provider database is not supported");
		}
	}
	
}
