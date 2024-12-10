package br.com.supera.lib.audit.domain.entity.mongo;

import java.io.Serializable;
import java.util.Objects;

import org.bson.types.ObjectId;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe Abstract para ser herdada por todas as collections (entidades) MongoDB
 *
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractAuditEntityMongo implements Serializable {

	private static final long serialVersionUID = -3473352269270736833L;
	
	@Id
	private ObjectId id;
	
	// Equals and Hashcode
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractAuditEntityMongo other = (AbstractAuditEntityMongo) obj;
		return Objects.equals(id, other.getId());
	}

}
