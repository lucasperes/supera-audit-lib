package br.com.supera.lib.audit.domain.entity.mongo;

import java.time.LocalDateTime;

import br.com.supera.lib.audit.annotation.TableProperties;
import br.com.supera.lib.audit.domain.entity.jpa.AbstractAuditEntity;
import br.com.supera.lib.audit.domain.enums.TypeOperationEnum;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Entity Mongo para guardar os dados de auditoria
 * 
 * @param <T> Tipo de Entidade a ser auditada
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@TableProperties(collectionName = TableAuditDataEntityMongo.NAME_COLLECTION)
public class TableAuditDataEntityMongo<T extends AbstractAuditEntity<?>> extends AbstractAuditEntityMongo<String> {

	private static final long serialVersionUID = 7678557512606960454L;

	public static final String NAME_COLLECTION = "logsAuditoria";

	@Id
	private String id;
	private String table;
	private LocalDateTime date;
	private String username;
	private TypeOperationEnum operation;
	private int version;
	private T entity;

}
