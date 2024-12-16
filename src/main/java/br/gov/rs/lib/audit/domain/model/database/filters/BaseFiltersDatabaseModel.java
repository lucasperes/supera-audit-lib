package br.gov.rs.lib.audit.domain.model.database.filters;

import java.util.List;
import java.util.Map;

import br.gov.rs.lib.audit.domain.enums.database.OperatorsDatabaseEnum;
import br.gov.rs.lib.audit.domain.model.BaseModel;
import br.gov.rs.lib.audit.domain.model.database.request.PaginationRequestModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Base para filtros em consultas no banco de dados
 */
@Getter @Setter
@AllArgsConstructor
public abstract class BaseFiltersDatabaseModel extends BaseModel {

	private static final long serialVersionUID = 9222382163557632179L;
	
	private static final int FIRST_PAGE = 0;
	private static final int ROWS_PER_PAGE = 20;
		
	@NotNull
	private PaginationRequestModel pagination;
	@NotNull
	private OperatorsDatabaseEnum operator;
	private List<ValueSorterModel> sorters;
	
	public abstract Map<String, Object> buildFilters();
	
	public BaseFiltersDatabaseModel() {
		this.pagination = PaginationRequestModel.builder()
				.page(FIRST_PAGE)
				.size(ROWS_PER_PAGE)
				.build();
		this.operator = OperatorsDatabaseEnum.AND;
	}
	
}
