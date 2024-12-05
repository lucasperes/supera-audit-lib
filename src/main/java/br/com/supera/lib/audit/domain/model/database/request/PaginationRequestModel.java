package br.com.supera.lib.audit.domain.model.database.request;

import br.com.supera.lib.audit.domain.model.BaseModel;
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

	private int page;
	private int size;
	
}
