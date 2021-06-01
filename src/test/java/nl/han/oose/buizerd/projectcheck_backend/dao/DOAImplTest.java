package nl.han.oose.buizerd.projectcheck_backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import java.io.Serializable;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DOAImplTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private UserTransaction userTransaction;

	private DAOImpl daoImpl;

	@BeforeEach
	void setUp() {
		daoImpl = new DAOImpl(entityManager, userTransaction);
	}

	@Test
	void create_roeptPersistAan(@Mock Object object) {
		daoImpl.create(object);
		Mockito.verify(entityManager).persist(object);
	}

	@Test
	void read_roeptFindAanEnGeeftOptionalTerug(@Mock Serializable serializable) {
		Class<Object> klasseType = Object.class;
		Optional<Object> expected = Optional.empty();
		Assertions.assertEquals(expected, daoImpl.read(klasseType, serializable));
		Mockito.verify(entityManager).find(klasseType, serializable);
	}

	@Test
	void update_roeptMergeAan(@Mock Object object) {
		daoImpl.update(object);
		Mockito.verify(entityManager).merge(object);
	}

	@Test
	void delete_roeptRemoveAan(@Mock Serializable serializable) {
		daoImpl.delete(serializable);
		Mockito.verify(entityManager).remove(serializable);
	}

}
