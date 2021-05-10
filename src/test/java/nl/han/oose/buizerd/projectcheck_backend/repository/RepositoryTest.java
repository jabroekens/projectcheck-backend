package nl.han.oose.buizerd.projectcheck_backend.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class RepositoryTest<T, K> {

	abstract void voegtToe(@Mock T t);

	abstract void geeft(@Mock K k);

	abstract void updatet(@Mock T t);

	abstract void verwijdert(@Mock K k);

}
