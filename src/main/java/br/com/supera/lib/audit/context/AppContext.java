package br.com.supera.lib.audit.context;

import br.com.supera.lib.audit.domain.model.app.SessionModel;
import lombok.experimental.UtilityClass;

/**
 * Classe Context para guardar a sessao da LIB
 */
@UtilityClass
public class AppContext {
	
	private static ThreadLocal<SessionModel> contextSession = new ThreadLocal<>();

	public static void setSession(SessionModel session) {
		synchronized (contextSession) {
			contextSession.set(session);
		}
	}

	public static SessionModel getCurrentSession() {
		synchronized (contextSession) {
			return contextSession.get();
		}
	}

	public static void clear() {
		synchronized (contextSession) {
			contextSession.set(null);
		}
	}
	
}
