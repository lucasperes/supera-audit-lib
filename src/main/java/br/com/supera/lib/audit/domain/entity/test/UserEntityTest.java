package br.com.supera.lib.audit.domain.entity.test;

import br.com.supera.lib.audit.domain.entity.jpa.AbstractAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_test", schema = "public")
public class UserEntityTest extends AbstractAuditEntity<Integer> {

	private static final long serialVersionUID = 4062278016219050608L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private String email;
	private String profile;
	
	@Override
	public Class<?> getClassType() {
		return UserEntityTest.class;
	}
	
}
