package br.com.supera.lib.audit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.supera.lib.audit.domain.entity.mongo.TableAuditDataEntityMongo;
import br.com.supera.lib.audit.domain.entity.mongo.UserModel;
import br.com.supera.lib.audit.domain.entity.test.UserEntityTest;
import br.com.supera.lib.audit.domain.enums.TypeOperationEnum;
import br.com.supera.lib.audit.domain.enums.database.ProviderDatabaseEnum;
import br.com.supera.lib.audit.domain.enums.database.SortEnum;
import br.com.supera.lib.audit.domain.model.database.config.DatabaseConnectionConfigModel;
import br.com.supera.lib.audit.domain.model.database.filters.FiltersTableAuditData;
import br.com.supera.lib.audit.domain.model.database.filters.ValueSorterModel;
import br.com.supera.lib.audit.domain.model.database.response.ResultPaginatedModel;

/**
 * Classe Teste para {@link TableAuditDataService}
 */
public class TableAuditDataServiceTest {

	private TableAuditDataService service;
	
	@Before
	public void initEach() {
		final var config = DatabaseConnectionConfigModel.builder()
				.host("localhost")
				.port(27017)
				.database("crie_logs")
				.username("")
				.password("")
				.provider(ProviderDatabaseEnum.MONGO_DB)
				.build();
		service = new TableAuditDataService(config);
	}
	
	@Test
	public void insertTest() {
		TableAuditDataEntityMongo<UserEntityTest> entity = new TableAuditDataEntityMongo<>();
		entity.setDate(LocalDateTime.now());
		entity.setEntity(UserEntityTest.builder()
				.id(1)
				.name("User Test Delete")
				.email("user.test@email.com")
				.build());
		entity.setOperation(TypeOperationEnum.INSERT);
		entity.setTable("USER");
		entity.setUser(new UserModel(1, "admin", "admin@email.com"));
		entity.setVersion(3);
		
		final TableAuditDataEntityMongo<UserEntityTest> result = service.<UserEntityTest>insert(entity);
		
		assertNotNull(result);
		assertNotNull(result.getId());
	}
	
	@Test
	public void listWithoutFiltersTest() {
		final var filters = new FiltersTableAuditData();
		filters.getPagination().setPage(2);
		filters.getPagination().setSize(3);
		ResultPaginatedModel<TableAuditDataEntityMongo<UserEntityTest>> result = service.<UserEntityTest>list(filters);
		
		assertNotNull(result);
		assertTrue(result.getTotalElements() > 0);
	}
	
	@Test
	public void listWithFiltersTest() {
		final var filters = new FiltersTableAuditData();
		filters.setOperation(TypeOperationEnum.DELETE);
		filters.setVersion(3);
		ResultPaginatedModel<TableAuditDataEntityMongo<UserEntityTest>> result = service.<UserEntityTest>list(filters);
		
		assertNotNull(result);
		assertTrue(result.getTotalElements() == 1);
	}
	
	@Test
	public void listWithFiltersResultNullTest() {
		final var filters = new FiltersTableAuditData();
		filters.setOperation(TypeOperationEnum.INSERT);
		filters.setVersion(3);
		ResultPaginatedModel<TableAuditDataEntityMongo<UserEntityTest>> result = service.<UserEntityTest>list(filters);
		
		assertNotNull(result);
		assertTrue(result.getTotalElements() == 0);
	}
	
	@Test
	public void listWithoutFiltersAndOrdersTest() {
		final var filters = new FiltersTableAuditData();
		filters.setSorters(List.of(new ValueSorterModel("version", SortEnum.DESC)));
		ResultPaginatedModel<TableAuditDataEntityMongo<UserEntityTest>> result = service.<UserEntityTest>list(filters);
		
		assertNotNull(result);
		assertTrue(result.getTotalElements() > 0);
		assertEquals(TypeOperationEnum.DELETE, result.getContent().get(0).getOperation());
	}
	
}
