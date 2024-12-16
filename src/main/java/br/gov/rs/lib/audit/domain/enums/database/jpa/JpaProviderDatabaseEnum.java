package br.gov.rs.lib.audit.domain.enums.database.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum Utilizado para guardar os tipos de provedores JPA suportados pela LIB
 */
@Getter
@AllArgsConstructor
public enum JpaProviderDatabaseEnum {

	H2("H2 Database", "org.h2.Driver", "jdbc:h2:mem:testdb", "org.hibernate.dialect.H2Dialect", "update"),
	POSTGRES("PostgreSQL Database", "org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s", "org.hibernate.dialect.PostgreSQL12Dialect", "none");
	
	private String name;
	private String driver;
	private String url;
	private String dialect;
	private String hbm2ddl;
	
}
