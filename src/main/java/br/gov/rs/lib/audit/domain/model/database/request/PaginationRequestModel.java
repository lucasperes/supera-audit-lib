package br.gov.rs.lib.audit.domain.model.database.request;

import br.gov.rs.lib.audit.domain.model.BaseModel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Request para consultar dados paginados do banco de dados
 * 
 */
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequestModel extends BaseModel {

	private static final long serialVersionUID = -4669269919628833236L;

	@Min(0)
	private int page;
	@Min(0)
	@Max(1000)
	private int size;
	
}
