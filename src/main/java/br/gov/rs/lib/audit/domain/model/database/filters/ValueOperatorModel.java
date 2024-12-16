package br.gov.rs.lib.audit.domain.model.database.filters;

import br.gov.rs.lib.audit.domain.enums.database.OperatorsDatabaseEnum;
import br.gov.rs.lib.audit.domain.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Model para guardar os valores de um filtro para consultas no banco
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValueOperatorModel extends BaseModel {
	
	private static final long serialVersionUID = 7242257033723460893L;
	
	private OperatorsDatabaseEnum operator;
	private Object value;

}
