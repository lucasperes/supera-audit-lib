package br.gov.rs.lib.audit.domain.entity.mongo;

import java.io.Serializable;

import br.gov.rs.lib.audit.domain.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Model para representar o Usuario que realiza operacoes no banco
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel extends BaseModel {

	private static final long serialVersionUID = 4545570175873473198L;

	private Serializable id;
	private String username;
	private String email;
	
}
