package br.com.supera.lib.audit.service;

import br.com.supera.lib.audit.domain.entity.jpa.AbstractAuditEntity;
import br.com.supera.lib.audit.domain.entity.mongo.TableAuditDataEntityMongo;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import br.com.supera.lib.audit.domain.model.database.filters.FiltersTableAuditData;
import br.com.supera.lib.audit.domain.model.database.response.ResultPaginatedModel;

/**
 * Classe Service para {@link TableAuditDataEntityMongo}
 */
public class TableAuditDataService extends AbstractAuditServiceBase {

	public TableAuditDataService(DatabaseConnectionConfigModel config) {
		super(config);
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractAuditEntity<?>> TableAuditDataEntityMongo<T> insert(TableAuditDataEntityMongo<T> entity) {
		return getConnection().insert(entity, TableAuditDataEntityMongo.class);
	}

	@SuppressWarnings("rawtypes")
	public <T extends AbstractAuditEntity<?>> ResultPaginatedModel<TableAuditDataEntityMongo> list(
			FiltersTableAuditData filters) {
		return getConnection().list(filters, TableAuditDataEntityMongo.class);
	}

}
