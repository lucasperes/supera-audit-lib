package br.com.supera.lib.audit.domain.model.database.config;

import br.com.supera.lib.audit.domain.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Model para guardar as propriedades de conexao com o banco de dados
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConnectionConfigModel extends BaseModel {

	private static final long serialVersionUID = 6350744765686548973L;
	
	private String host;
	private int port;
	private String username;
	private String password;
	private String database;
	
}