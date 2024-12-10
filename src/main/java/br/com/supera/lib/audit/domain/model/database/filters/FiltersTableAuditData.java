package br.com.supera.lib.audit.domain.model.database.filters;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.supera.lib.audit.domain.entity.mongo.TableAuditDataEntityMongo;
import br.com.supera.lib.audit.domain.enums.TypeOperationEnum;
import br.com.supera.lib.audit.domain.enums.database.OperatorsDatabaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Filters para filtrar as consultas da entidade
 * {@link TableAuditDataEntityMongo} no banco de dados
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltersTableAuditData extends BaseFiltersDatabaseModel {

	private static final long serialVersionUID = 2555466308190839159L;

	private String id;
	private String table;
	private LocalDateTime dateStart;
	private LocalDateTime dateEnd;
	private String username;
	private String email;
	private TypeOperationEnum operation;
	private int version;
	
	@Override
	public Map<String, Object> buildFilters() {
		final var filters = new HashMap<String, Object>();
		filters.put("id", new ValueOperatorModel(OperatorsDatabaseEnum.EQ, id));
		filters.put("table", new ValueOperatorModel(OperatorsDatabaseEnum.EQ, table));
		filters.put("date", List.of(
				new ValueOperatorModel(OperatorsDatabaseEnum.GTE, dateStart),
				new ValueOperatorModel(OperatorsDatabaseEnum.LTE, dateEnd)));
		filters.put("user.username", username);
		filters.put("user.email", email);
		
		return filters;
	}

}
