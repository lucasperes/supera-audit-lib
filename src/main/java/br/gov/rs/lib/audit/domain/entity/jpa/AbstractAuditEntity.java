package br.gov.rs.lib.audit.domain.entity.jpa;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.gov.rs.lib.audit.domain.entity.EntityBase;
import br.gov.rs.lib.audit.listener.AuditEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe Abstract para ser herdada por todas as classes passiveis de auditoria
 *
 * @param <ID> Tipo de Chave Primaria
 */
@Getter
@Setter
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditEntityListener.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "_class")
public abstract class AbstractAuditEntity<ID> extends EntityBase {

	private static final long serialVersionUID = -5090870804974529985L;

	// NAMES COLUMNS DATABASE
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_VERSAO = "NUM_VERSAO";
	public static final String COLUMN_IS_ATIVO = "IS_ATIVO";

	/**
	 * Deve retornar o ID da Entidade
	 * 
	 * @return <ID>
	 */
	public abstract ID getId();

	/**
	 * Deve definir o ID da Entidade
	 * 
	 * @param <ID>
	 */
	public abstract void setId(ID id);

	/**
	 * Deve retornar o tipo de classe concreta de implementacao
	 * 
	 * @return {@link Class}
	 */
	@JsonIgnore
	public abstract Class<?> getClassType();

	@NotNull
	@Column(name = COLUMN_VERSAO, nullable = false)
	private Integer versao;
	
	@NotNull
	@Column(name = COLUMN_IS_ATIVO, nullable = false)
	private Boolean isAtivo;

	@Transient
	private String _class;

	public AbstractAuditEntity() {
		this._class = getClassType().getName();
	}

	// Equals and Hashcode

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractAuditEntity<ID> other = (AbstractAuditEntity<ID>) obj;
		return Objects.equals(getId(), other.getId());
	}

}
