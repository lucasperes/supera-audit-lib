package br.com.supera.lib.audit.domain.enums.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum Utilizado para guardar os provedores de banco de dados suportados pela LIB
 */
@Getter
@AllArgsConstructor
public enum ProviderDatabaseEnum {

	JPA("JPA"),
	MONGO_DB("Mongo DB");
	
	private String description;
	
}
