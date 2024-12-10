package br.com.supera.lib.audit.exception;

/**
 * Classe Exception para operacoes com objetos JSON
 */
public class JSONSerializerException extends RuntimeException {

	private static final long serialVersionUID = -6740744207764884962L;
	
	/**
	 * @param msg {@link String}
	 */
	public JSONSerializerException(String msg) {
		super(msg);
	}

	/**
	 * @param msg {@link String}
	 * @param cause {@link Throwable}
	 */
	public JSONSerializerException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
