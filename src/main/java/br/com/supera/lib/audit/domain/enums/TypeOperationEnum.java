package br.com.supera.lib.audit.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeOperationEnum {
	
	INSERT(0, "Insert"),
	UPDATE(1, "Update"),
	DELETE(2, "Delete"),
	SELECT(3, "Select");
	
	private int code;
	private String description;

}
