package nl.han.oose.buizerd.projectcheck_backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import java.io.Serializable;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DOAImplTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private UserTransaction userTransaction;

	private DAOImpl sut;

	@BeforeEach
	void setUp() {
		sut = new DAOImpl(entityManager, userTransaction);
	}

	@Test
	void create_roeptPersistAan(@Mock Object object) {
		sut.create(object);
		verify(entityManager).persist(object);
	}

	@Test
	void read_roeptFindAanEnGeeftOptionalTerug(@Mock Serializable serializable) {
		Class<Object> klasseType = Object.class;
		Optional<Object> expected = Optional.empty();
		assertEquals(expected, sut.read(klasseType, serializable));
		verify(entityManager).find(klasseType, serializable);
	}

	@Test
	void update_roeptMergeAan(@Mock Object object) {
		sut.update(object);
		verify(entityManager).merge(object);
	}

	@Test
	void delete_roeptRemoveAan(@Mock Serializable serializable) {
		sut.delete(serializable);
		verify(entityManager).remove(serializable);
	}

}
