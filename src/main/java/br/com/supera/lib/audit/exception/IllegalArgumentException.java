package br.com.supera.lib.audit.exception;

/**
 * Classe Exception para argumentos de parametros invalidos
 */
public class IllegalArgumentException extends RuntimeException {

	private static final long serialVersionUID = -6740744207764884962L;
	
	/**
	 * @param msg {@link String}
	 */
	public IllegalArgumentException(String msg) {
		super(msg);
	}

	/**
	 * @param msg {@link String}
	 * @param cause {@link Throwable}
	 */
	public IllegalArgumentException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
