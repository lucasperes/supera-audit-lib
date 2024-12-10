package br.com.supera.lib.audit.service;

import br.com.supera.lib.audit.config.MongoConnectionFactory;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Classe Service base para ser extendida pelas classes Services da LIB
 */
public abstract class AbstractAuditServiceBase {

	@Getter(value = AccessLevel.PROTECTED)
	private MongoConnectionFactory connection;
	
	public AbstractAuditServiceBase(DatabaseConnectionConfigModel config) {
		this.connection = new MongoConnectionFactory(config);
	}
	
}
