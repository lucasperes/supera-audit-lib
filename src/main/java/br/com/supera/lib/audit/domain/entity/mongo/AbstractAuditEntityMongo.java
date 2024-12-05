package br.com.supera.lib.audit.domain.entity.mongo;

import java.io.Serializable;
import java.util.Objects;

import br.com.supera.lib.audit.domain.entity.jpa.AbstractAuditEntity;

/**
 * Classe Abstract para ser herdada por todas as collections (entidades) MongoDB
 *
 * @param <ID> Tipo da Chave Primaria
 * 
 */
public abstract class AbstractAuditEntityMongo<ID> implements Serializable {

	private static final long serialVersionUID = -3473352269270736833L;
	
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
