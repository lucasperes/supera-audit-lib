package br.com.supera.lib.audit.config.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import br.com.supera.lib.audit.context.AppContext;
import br.com.supera.lib.audit.domain.entity.mongo.UserModel;
import br.com.supera.lib.audit.domain.entity.test.DadosPessoalEntityTest;
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
	private UserModel userLogged;
	private DatabaseConnectionConfigModel configMongo;

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
		configMongo = DatabaseConnectionConfigModel.builder()
				.host("localhost")
				.port(27017)
				.database("crie_logs")
				.username("")
				.password("")
				.provider(ProviderDatabaseEnum.MONGO_DB)
				.build();
		userLogged = new UserModel(1, "admin2.test", "admin2.test@email.com");
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
	public void insertInBackgroundTest() throws InterruptedException {
		SessionModel session = SessionModel.builder()
				.databaseConnection(configMongo)
				.userLogged(userLogged)
				.executeInBackground(true)
				.build();
		AppContext.setSession(session);
		
		final var entity = new UserEntityTest();
		entity.setName("Admin");
		entity.setEmail("admin@email.com");
		entity.setProfile("DEVELOPER");
		
		UserEntityTest result = connection.insert(entity, UserEntityTest.class);
		connection.closeFactory();
		
		Thread.sleep(2000);
		
		assertNotNull(result);
		assertNotNull(result.getId());
	}
	
	@Test
	public void insertDadosPessoalTest() {
		final var entity = new DadosPessoalEntityTest();
		entity.setNome("Joao Martins");
		entity.setCpf("000.000.000-99");
		
		DadosPessoalEntityTest result = connection.insert(entity, DadosPessoalEntityTest.class);
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
	public void updateDadosPessoalTest() {
		EntityManager manager = connection.createManager();
		manager.getTransaction().begin();
		
		final var entity = new DadosPessoalEntityTest();
		entity.setNome("Joao Martins");
		entity.setCpf("000.000.000-99");
		
		manager.persist(entity);
		manager.getTransaction().commit();
		
		manager.getTransaction().begin();
		entity.setNome("Admin Update");
		DadosPessoalEntityTest result = manager.merge(entity);
		manager.getTransaction().commit();
		
		manager.close();
		connection.closeFactory();
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals("Admin Update", result.getNome());
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
	
	@Test
	public void deleteDadosPessoalTest() {
		EntityManager manager = connection.createManager();
		manager.getTransaction().begin();
		
		final var entity = new DadosPessoalEntityTest();
		entity.setNome("Joao Martins");
		entity.setCpf("000.000.000-99");
		
		manager.persist(entity);
		manager.getTransaction().commit();
		
		manager.getTransaction().begin();
		manager.remove(entity);
		manager.getTransaction().commit();
		
		DadosPessoalEntityTest result = manager.find(DadosPessoalEntityTest.class, entity.getId());
		
		manager.close();
		connection.closeFactory();
		
		assertNull(result);
	}
	
	@Test
	public void logicalDeleteDadosPessoalTest() {
		EntityManager manager = connection.createManager();
		manager.getTransaction().begin();
		
		final var entity = new DadosPessoalEntityTest();
		entity.setNome("Joao Martins");
		entity.setCpf("000.000.000-99");
		
		manager.persist(entity);
		manager.getTransaction().commit();
		
		manager.getTransaction().begin();
		entity.setNome("Joao Martins Delete");
		entity.setIsAtivo(false);
		DadosPessoalEntityTest result = manager.merge(entity);
		manager.getTransaction().commit();
		
		manager.close();
		connection.closeFactory();
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertEquals("Joao Martins Delete", result.getNome());
		assertFalse(result.getIsAtivo());
	}
	
}
