package br.com.supera.lib.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableProperties {

	/**
	 * Deve retornar o nome da collection no mongo
	 *
	 * @return {@link String}
	 */
	String collectionName();
	
}
