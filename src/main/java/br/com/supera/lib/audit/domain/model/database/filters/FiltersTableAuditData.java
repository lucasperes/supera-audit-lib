package br.com.supera.lib.audit.domain.model.database.filters;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

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
	private Integer version;
	
	@Override
	public Map<String, Object> buildFilters() {
		final var filters = new HashMap<String, Object>();
		if(id != null && !id.isBlank()) {
			filters.put("id", new ValueOperatorModel(OperatorsDatabaseEnum.EQ, new ObjectId(id)));
		}
		if(table != null) {
			filters.put("table", new ValueOperatorModel(OperatorsDatabaseEnum.EQ, table));
		}
		if(dateStart != null && dateEnd != null) {
			filters.put("date", List.of(
					new ValueOperatorModel(OperatorsDatabaseEnum.GTE, dateStart),
					new ValueOperatorModel(OperatorsDatabaseEnum.LTE, dateEnd)));
		}
		if(username != null) {
			filters.put("user.username", username);
		}
		if(email != null) {
			filters.put("user.email", email);
		}
		if(operation != null) {
			filters.put("operation", new ValueOperatorModel(OperatorsDatabaseEnum.EQ, operation));
		}
		if(version != null) {
			filters.put("version", new ValueOperatorModel(OperatorsDatabaseEnum.EQ, version));
		}
		
		return filters;
	}

}
