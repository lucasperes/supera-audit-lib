package br.com.supera.lib.audit.listener;

@FunctionalInterface
public interface IExecuteActionListener {

	/**
	 * Should execute an action
	 */
	void execute();
	
}
