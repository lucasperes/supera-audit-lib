package br.gov.rs.lib.audit.listener;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.rs.lib.audit.context.AppContext;
import br.gov.rs.lib.audit.domain.entity.jpa.AbstractAuditEntity;
import br.gov.rs.lib.audit.domain.entity.mongo.TableAuditDataEntityMongo;
import br.gov.rs.lib.audit.domain.enums.TypeOperationEnum;
import br.gov.rs.lib.audit.domain.model.app.SessionModel;
import br.gov.rs.lib.audit.service.TableAuditDataService;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * Classe Listener para ouvir os eventos de CRUD no banco de dados
 */
public class AuditEntityListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditEntityListener.class);
	
	private TableAuditDataService service;
	private boolean isBuild = false;
	private SessionModel session;
	
	private void build() {
		if(AppContext.getCurrentSession() != null) {
			session = AppContext.getCurrentSession();
			if(session.getDatabaseConnection() != null) {
				service = new TableAuditDataService(session.getDatabaseConnection());
				isBuild = true;
			} else {
				isBuild = false;
			}
		} else {			
			isBuild = false;
		}
	}
	
	@PrePersist
	public void beforePersist(AbstractAuditEntity<?> entity) {
		entity.setVersao(0);
		entity.setIsAtivo(true);
	}
	
	@PostPersist
	public void afterPersist(AbstractAuditEntity<?> entity) {
		IExecuteActionListener action = () -> {
			Table table = entity.getClass().getAnnotation(Table.class);
			// Construct Audit Insert
			TableAuditDataEntityMongo<?> entityAudit = TableAuditDataEntityMongo.builder()
					.date(LocalDateTime.now())
					.entity(entity)
					.operation(TypeOperationEnum.INSERT)
					.table(table.name())
					.user(session.getUserLogged())
					.version(entity.getVersao())
					.build();
			
			service.insert(entityAudit);
		};
		
		executeActionListener(action);
	}
	
	@PreUpdate
	public void beforeUpdate(AbstractAuditEntity<?> entity) {
		entity.setVersao(entity.getVersao() + 1);
	}

	@PostUpdate
	public void afterUpdate(AbstractAuditEntity<?> entity) {
		IExecuteActionListener action = () -> {
			// Logical Exclusion
			if(Boolean.FALSE.equals(entity.getIsAtivo())) {
				insertLogDelete(entity);
			} else {
				Table table = entity.getClass().getAnnotation(Table.class);
				// Construct Audit Update
				TableAuditDataEntityMongo<?> entityAudit = TableAuditDataEntityMongo.builder()
						.date(LocalDateTime.now())
						.entity(entity)
						.operation(TypeOperationEnum.UPDATE)
						.table(table.name())
						.user(session.getUserLogged())
						.version(entity.getVersao())
						.build();
				
				service.insert(entityAudit);
			}
		};
		
		executeActionListener(action);
	}
	
	@PreRemove
	public void beforeRemove(AbstractAuditEntity<?> entity) {
		entity.setVersao(entity.getVersao() + 1);
	}

	@PostRemove
	public void afterRemove(AbstractAuditEntity<?> entity) {
		executeActionListener(() -> {
			insertLogDelete(entity);
		});
	}
	
	private void insertLogDelete(AbstractAuditEntity<?> entity) {
		Table table = entity.getClass().getAnnotation(Table.class);
		// Construct Audit Update
		TableAuditDataEntityMongo<?> entityAudit = TableAuditDataEntityMongo.builder()
				.date(LocalDateTime.now())
				.entity(entity)
				.operation(TypeOperationEnum.DELETE)
				.table(table.name())
				.user(session.getUserLogged())
				.version(entity.getVersao())
				.build();
		
		service.insert(entityAudit);
	}
	
	@PostLoad
	public void afterLoad(Object entity) {
		System.out.println("Post load");
	}
	
	private void executeActionListener(IExecuteActionListener action) {
		try {
			build();
			if(isBuild) {
				// Asyncrono
				if(session.isExecuteInBackground()) {
					var executor = Executors.newVirtualThreadPerTaskExecutor();
					executor.submit(() -> {
						action.execute();
						executor.shutdown();
					});
				}
				// Syncrono
				else {
					action.execute();
				}
			} else {
				LOGGER.warn("WARN | Application Context not initialized");
			}
		} catch(Exception err) {			
			LOGGER.warn("ERROR | Is not possible save logs on mongo database {}", err);
		}
	}
	
}
