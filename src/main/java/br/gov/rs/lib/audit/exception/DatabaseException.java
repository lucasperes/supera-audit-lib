package br.gov.rs.lib.audit.exception;

/**
 * Classe Exception para operacoes com o banco de dados
 */
public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = -6740744207764884962L;
	
	/**
	 * @param msg {@link String}
	 */
	public DatabaseException(String msg) {
		super(msg);
	}

	/**
	 * @param msg {@link String}
	 * @param cause {@link Throwable}
	 */
	public DatabaseException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
