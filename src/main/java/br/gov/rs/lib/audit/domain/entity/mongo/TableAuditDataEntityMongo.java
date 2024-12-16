package br.gov.rs.lib.audit.domain.entity.mongo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.gov.rs.lib.audit.annotation.TableProperties;
import br.gov.rs.lib.audit.domain.entity.jpa.AbstractAuditEntity;
import br.gov.rs.lib.audit.domain.enums.TypeOperationEnum;
import br.gov.rs.lib.audit.utils.ConstantsAuditUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Entity Mongo para guardar os dados de auditoria
 * 
 * @param <T> Tipo de Entidade a ser auditada
 */
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@TableProperties(collectionName = TableAuditDataEntityMongo.NAME_COLLECTION)
public class TableAuditDataEntityMongo<T extends AbstractAuditEntity<?>> extends AbstractAuditEntityMongo {

	private static final long serialVersionUID = 7678557512606960454L;

	public static final String NAME_COLLECTION = "logs_auditoria";

	private String table;
	@JsonFormat(pattern = ConstantsAuditUtils.PATTER_DATE_TIME_AMERICAN)
	private LocalDateTime date;
	private UserModel user;
	private TypeOperationEnum operation;
	private int version;
	private T entity;

}
