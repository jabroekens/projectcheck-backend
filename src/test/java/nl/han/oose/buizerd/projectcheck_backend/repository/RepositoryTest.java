package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.io.Serializable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class RepositoryTest<T, K extends Serializable> {

	abstract void voegtToe(@Mock T t);

	abstract void geeft(@Mock K k);

	abstract void updatet(@Mock T t);

	abstract void verwijdert(@Mock K k);

}
