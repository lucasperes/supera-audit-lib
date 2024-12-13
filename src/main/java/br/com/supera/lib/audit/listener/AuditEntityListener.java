package br.com.supera.lib.audit.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

/**
 * Classe Listener para ouvir os eventos de CRUD no banco
 */
public class AuditEntityListener {

	@PostPersist
	public void afterPersist(Object entity) {
		System.out.println("Post persist");
	}

	@PostUpdate
	public void afterUpdate(Object entity) {
		System.out.println("Post update");
	}

	@PostRemove
	public void afterRemove(Object entity) {
		System.out.println("Post remove");
	}
	
}
