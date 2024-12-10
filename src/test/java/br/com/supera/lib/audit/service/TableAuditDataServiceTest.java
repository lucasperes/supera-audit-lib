package br.com.supera.lib.audit.service;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import br.com.supera.lib.audit.domain.entity.UserEntityTest;
import br.com.supera.lib.audit.domain.entity.mongo.TableAuditDataEntityMongo;
import br.com.supera.lib.audit.domain.entity.mongo.UserModel;
import br.com.supera.lib.audit.domain.enums.TypeOperationEnum;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;

/**
 * Classe Teste para {@link TableAuditDataService}
 */
public class TableAuditDataServiceTest {

	private TableAuditDataService service;
	
	@Before
	public void initAll() {
		final var config = DatabaseConnectionConfigModel.builder()
				.host("localhost")
				.port(27017)
				.database("crie_logs")
				.username("")
				.password("")
				.build();
		service = new TableAuditDataService(config);
	}
	
	@Test
	public void insertTest() {
		TableAuditDataEntityMongo<UserEntityTest> entity = new TableAuditDataEntityMongo<>();
		entity.setDate(LocalDateTime.now());
		entity.setEntity(UserEntityTest.builder()
				.id(1)
				.name("User Test")
				.email("user.test@email.com")
				.build());
		entity.setOperation(TypeOperationEnum.INSERT);
		entity.setTable("USER");
		entity.setUser(new UserModel(1, "admin", "admin@email.com"));
		entity.setVersion(0);
		
		final TableAuditDataEntityMongo<UserEntityTest> result = service.<UserEntityTest>insert(entity);
		
		assertNotNull(result);
		assertNotNull(result.getId());
	}
	
}
