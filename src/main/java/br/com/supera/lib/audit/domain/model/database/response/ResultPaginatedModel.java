package br.com.supera.lib.audit.domain.model.database.response;

import java.util.List;

import br.com.supera.lib.audit.domain.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Response para retornar dados paginados do banco de dados
 * 
 * @param <T> Tipo de Dados de Resposta
 */
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultPaginatedModel<T> extends BaseModel {

	private static final long serialVersionUID = -7644051652750646902L;

	private int page;
	private int size;
	private int totalPages;
	private int totalElements;
	private List<T> content;
	
}
