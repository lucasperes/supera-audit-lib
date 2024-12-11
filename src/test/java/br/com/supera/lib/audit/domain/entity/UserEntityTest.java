package br.com.supera.lib.audit.domain.entity;

import br.com.supera.lib.audit.annotation.TableProperties;
import br.com.supera.lib.audit.domain.entity.jpa.AbstractAuditEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@TableProperties(collectionName = "user_logs")
public class UserEntityTest extends AbstractAuditEntity<Integer> {

	private static final long serialVersionUID = 4062278016219050608L;
	
	private Integer id;
	private String name;
	private String email;
	
	@Override
	public Class<?> getClassType() {
		return UserEntityTest.class;
	}
	
}
