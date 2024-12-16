package br.gov.rs.lib.audit.domain.enums.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum Utilizado para guardar os tipos de ordenacao em consultas no banco
 */
@Getter
@AllArgsConstructor
public enum SortEnum {
	
	ASC("Ascending"),
	DESC("Descending");
	
	private String description;

}
