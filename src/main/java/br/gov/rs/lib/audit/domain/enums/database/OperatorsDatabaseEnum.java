package br.gov.rs.lib.audit.domain.enums.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperatorsDatabaseEnum {
	
	EQ("eq", "=", "Equals"),
	GT("gt", ">", "Greater Than"),
	GTE("gte", ">=", "Grater Than or Equals"),
	LT("lt", "<", "Less Than"),
	LTE("lte", "<=", "Less Than or Equals"),
	AND("and", "and", "Logical Combination And"),
	OR("or", "or", "Alternative Conditions Or");
	
	private String codeMongo;
	private String codeSql;
	private String description;

}
