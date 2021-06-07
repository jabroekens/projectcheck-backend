package nl.han.oose.buizerd.projectcheck_backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import jakarta.persistence.EntityManager;
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

	private DAOImpl sut;

	@BeforeEach
	void setUp() {
		sut = new DAOImpl();
		sut.setEntityManager(entityManager);
	}

	@Test
	void create_roeptPersistAan(@Mock Object object) {
		sut.create(object);
		verify(entityManager).persist(object);
	}

	@Test
	void read_roeptFindAanEnGeeftOptionalTerug(@Mock Serializable serializable) {
		var klasseType = Object.class;
		var expected = Optional.empty();

		var actual = sut.read(klasseType, serializable);

		assertEquals(expected, actual);
		verify(entityManager).find(klasseType, serializable);
	}

	@Test
	void update_roeptMergeAan(@Mock Object object) {
		sut.update(object);
		verify(entityManager).merge(object);
	}

	@Test
	void delete_roeptRemoveAan(@Mock Object object) {
		sut.delete(object);
		verify(entityManager).remove(object);
	}

}
