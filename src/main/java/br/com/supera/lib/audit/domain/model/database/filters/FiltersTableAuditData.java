package br.com.supera.lib.audit.domain.model.database.filters;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import br.com.supera.lib.audit.domain.entity.mongo.TableAuditDataEntityMongo;
import br.com.supera.lib.audit.domain.enums.TypeOperationEnum;
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
	private TypeOperationEnum operation;
	private int version;
	
	@Override
	public Map<String, Object> buildFilters() {
		final var filters = new HashMap<String, Object>();
		filters.put("id", id);
		filters.put("table", table);
		filters.put("date", Map.entry(null, null));
		
		return filters;
	}

}
