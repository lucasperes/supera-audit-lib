package br.gov.rs.lib.audit.config;

import br.gov.rs.lib.audit.domain.entity.EntityBase;
import br.gov.rs.lib.audit.domain.model.database.filters.BaseFiltersDatabaseModel;
import br.gov.rs.lib.audit.domain.model.database.response.ResultPaginatedModel;

/**
 * Interface para conexao com banco de dados
 */
public interface IDatabaseConnectionFactory {

	/**
	 * Deve iniciar as configuracao de conexao com o banco
	 */
	void buildClient();

	/**
	 * Deve inserir uma Entidade no banco de dados
	 *
	 * @param <T>    Tipo de Entidade a ser persistida
	 * @param entity <T>
	 * @param type   {@link Class} of <T>
	 * @return <T>
	 */
	<T extends EntityBase> T insert(T entity, Class<T> type);

	/**
	 *
	 * @param <T>     Tipo de Entidade a ser listada
	 * @param filters {@link BaseFiltersDatabaseModel}
	 * @param type    {@link Class} of <T>
	 * @return {@link ResultPaginatedModel} of <T>
	 */
	<T extends EntityBase> ResultPaginatedModel<T> list(BaseFiltersDatabaseModel filters, Class<T> type);

}
