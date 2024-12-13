package br.com.supera.lib.audit.listener;

import org.junit.Before;
import org.junit.Test;

import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;

/**
 * Classe Test para {@link AuditEntityListener}
 */
public class AuditEntityListenerTest {
	
	private AuditEntityListener listener;

	@Before
	public void initEach() {
		final var config = DatabaseConnectionConfigModel.builder()
				.host("localhost")
				.port(27017)
				.database("crie_logs")
				.username("")
				.password("")
				.build();
		listener = new AuditEntityListener();
	}
	
	@Test
	public void afterPersistTest() {
		
	}
	
}
