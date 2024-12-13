package br.com.supera.lib.audit.config.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import br.com.supera.lib.audit.domain.entity.UserEntityTest;
import br.com.supera.lib.audit.domain.enums.database.ProviderDatabaseEnum;
import br.com.supera.lib.audit.domain.enums.database.jpa.JpaProviderDatabaseEnum;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;

/**
 * Classe teste para {@link JpaConnectionFactoryImpl}
 */
public class JpaConnectionFactoryImplTest {

	private JpaConnectionFactoryImpl connection;

	@Before
	public void initEach() {
		final var config = DatabaseConnectionConfigModel.builder()
				.host("localhost")
				.port(27017)
				.database("crie_logs")
				.username("sa")
				.password("")
				.provider(ProviderDatabaseEnum.JPA)
				.providerJpa(JpaProviderDatabaseEnum.H2)
				.build();
		connection = new JpaConnectionFactoryImpl(config);
	}
	
	@Test
	public void insertTest() {
		final var entity = new UserEntityTest();
		entity.setName("Admin");
		entity.setEmail("admin@email.com");
		entity.setVersao(0);
		
		UserEntityTest result = connection.insert(entity, UserEntityTest.class);
		
		assertNotNull(result);
		assertNotNull(result.getId());
	}
	
}
