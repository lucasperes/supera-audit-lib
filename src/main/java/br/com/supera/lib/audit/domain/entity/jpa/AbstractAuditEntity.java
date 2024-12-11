package br.com.supera.lib.audit.domain.entity.jpa;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
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
@Audited
@Getter @Setter
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "_class")
public abstract class AbstractAuditEntity<ID> implements Serializable {

	private static final long serialVersionUID = -5090870804974529985L;

	// NAMES COLUMNS DATABASE
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_VERSAO = "NUM_VERSAO";
	
	/**
	 * Deve retornar o ID da Entidade
	 * @return <ID>
	 */
	public abstract ID getId();
	
	/**
	 * Deve definir o ID da Entidade
	 * @param <ID>
	 */
	public abstract void setId(ID id);
	
	/**
	 * Deve retornar o tipo de classe concreta de implementacao
	 * 
	 * @return {@link Class}
	 */
	public abstract Class<?> getClassType();
	
	@NotNull
	@Column(name = COLUMN_VERSAO, nullable = false)
	private Integer versao;
	
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
