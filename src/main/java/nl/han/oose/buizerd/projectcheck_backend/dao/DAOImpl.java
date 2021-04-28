package nl.han.oose.buizerd.projectcheck_backend.dao;

import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class DAOImpl<T, K> implements DAO<T, K> {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction transaction;

	public void create(T t) {
		doTransaction(entityManager -> entityManager.persist(t));
	}

	public Optional<T> read(Class<T> klasseType, K k) {
		return Optional.ofNullable(entityManager.find(klasseType, k));
	}

	public void update(T t) {
		doTransaction(entityManager -> entityManager.merge(t));
	}

	public void delete(K k) {
		doTransaction(entityManager -> entityManager.remove(k));
	}

	private void doTransaction(Consumer<EntityManager> consumer) throws RuntimeException {
		try {
			transaction.begin();
			consumer.accept(entityManager);
			transaction.commit();
		} catch (Exception e) {
			try {
				transaction.rollback();
			} catch (SystemException se) {
				throw new RuntimeException(se);
			}

			throw new RuntimeException(e);
		}
	}

}
