package br.com.supera.lib.audit.domain.model.app;

import br.com.supera.lib.audit.domain.entity.mongo.UserModel;
import br.com.supera.lib.audit.domain.model.BaseModel;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Model para guardar os dados da sessao da LIB
 */
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionModel extends BaseModel {

	private static final long serialVersionUID = -2121594644463312652L;

	private DatabaseConnectionConfigModel databaseConnection;
	private UserModel userLogged;
	private boolean executeInBackground;
	
}
