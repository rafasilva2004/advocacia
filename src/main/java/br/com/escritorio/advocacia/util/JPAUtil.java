package br.com.escritorio.advocacia.util;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAUtil {

	private static final ThreadLocal<EntityManager> entityManagerThread = new ThreadLocal<EntityManager>();
	private static final ThreadLocal<EntityTransaction> transactionThread = new ThreadLocal<EntityTransaction>();

	private static EntityManagerFactory entityManagerFactory;

	public static void init(String unitName, Map<String, String> parametros) {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory(
					unitName, parametros);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void init(String UnitName) {
		init(UnitName, null);
	}

	public static void closeEntityManagerFactory() {
		entityManagerFactory.close();
	}

	public static EntityManager getEntityManager() {
		if (entityManagerThread.get() == null) {
			EntityManager em = entityManagerFactory.createEntityManager();

			entityManagerThread.set(em);
		}
		return (EntityManager) entityManagerThread.get();
	}

	public static void closeEntityManager() {
		EntityManager em = entityManagerThread.get();
		if (em != null && em.isOpen()) {
			entityManagerThread.set(null);
			em.close();
		}
		entityManagerThread.remove();
		transactionThread.remove();
	}

	public static void beginTransaction() {
		EntityTransaction transaction = getEntityManager().getTransaction();
		transaction.begin();
		transactionThread.set(transaction);
	}

	public static void commitTransaction() {
		EntityTransaction transaction = (EntityTransaction) transactionThread
				.get();
		if (transaction != null && transaction.isActive()) {
			transaction.commit();
			transactionThread.set(null);
		}
	}

	public static void rollbackTransaction() {
		EntityTransaction transaction = (EntityTransaction) transactionThread
				.get();
		if (transaction != null && transaction.isActive()) {
			transaction.rollback();
			transactionThread.set(null);
		}
	}
}
