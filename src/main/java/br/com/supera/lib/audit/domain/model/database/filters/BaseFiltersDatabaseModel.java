package br.com.supera.lib.audit.domain.model.database.filters;

import java.util.List;
import java.util.Map;

import br.com.supera.lib.audit.domain.enums.database.OperatorsDatabaseEnum;
import br.com.supera.lib.audit.domain.model.BaseModel;
import br.com.supera.lib.audit.domain.model.database.request.PaginationRequestModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Base para filtros em consultas no banco de dados
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseFiltersDatabaseModel extends BaseModel {

	private static final long serialVersionUID = 9222382163557632179L;

	@NotNull
	private PaginationRequestModel pagination;
	@NotNull
	private OperatorsDatabaseEnum operator;
	private List<ValueSorterModel> sorters;
	
	public abstract Map<String, Object> buildFilters();
	
}
