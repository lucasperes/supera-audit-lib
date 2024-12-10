package br.com.supera.lib.audit.domain.model.database.filters;

import br.com.supera.lib.audit.domain.enums.database.SortEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Model para guardar os valores de ordenacao de consultas no banco de dados
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValueSorterModel {

	private String nameAttr;
	private SortEnum type;
	
}
