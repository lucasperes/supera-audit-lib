package br.com.supera.lib.audit.config.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import br.com.supera.lib.audit.context.AppContext;
import br.com.supera.lib.audit.domain.entity.mongo.UserModel;
import br.com.supera.lib.audit.domain.entity.test.UserEntityTest;
import br.com.supera.lib.audit.domain.enums.database.ProviderDatabaseEnum;
import br.com.supera.lib.audit.domain.enums.database.jpa.JpaProviderDatabaseEnum;
import br.com.supera.lib.audit.domain.model.app.SessionModel;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import jakarta.persistence.EntityManager;

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
		
		// Seta o Contexto
		final var configMongo = DatabaseConnectionConfigModel.builder()
				.host("localhost")
				.port(27017)
				.database("crie_logs")
				.username("")
				.password("")
				.provider(ProviderDatabaseEnum.MONGO_DB)
				.build();
		UserModel userLogged = new UserModel(1, "admin2.test", "admin2.test@email.com");
		SessionModel session = SessionModel.builder()
				.databaseConnection(configMongo)
				.userLogged(userLogged)
				.build();
		AppContext.setSession(session);
	}
	
	@Test
	public void insertTest() {
		final var entity = new UserEntityTest();
		entity.setName("Admin");
		entity.setEmail("admin@email.com");
		entity.setProfile("DEVELOPER");
		
		UserEntityTest result = connection.insert(entity, UserEntityTest.class);
		connection.closeFactory();
		
		assertNotNull(result);
		assertNotNull(result.getId());
	}
	
	@Test
	public void updateTest() {
		EntityManager manager = connection.createManager();
		manager.getTransaction().begin();
		
		final var entity = new UserEntityTest();
		entity.setName("Admin");
		entity.setEmail("admin@email.com");
		entity.setProfile("DEVELOPER");
		
		manager.persist(entity);
		manager.getTransaction().commit();
		
		manager.getTransaction().begin();
		entity.setName("Admin Update");
		UserEntityTest result = manager.merge(entity);
		manager.getTransaction().commit();
		
		manager.close();
		connection.closeFactory();
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals("Admin Update", result.getName());
	}
	
	@Test
	public void deleteTest() {
		EntityManager manager = connection.createManager();
		manager.getTransaction().begin();
		
		final var entity = new UserEntityTest();
		entity.setName("Admin");
		entity.setEmail("admin@email.com");
		entity.setProfile("DEVELOPER");
		
		manager.persist(entity);
		manager.getTransaction().commit();
		
		manager.getTransaction().begin();
		manager.remove(entity);
		manager.getTransaction().commit();
		
		UserEntityTest result = manager.find(UserEntityTest.class, entity.getId());
		
		manager.close();
		connection.closeFactory();
		
		assertNull(result);
	}
	
}
