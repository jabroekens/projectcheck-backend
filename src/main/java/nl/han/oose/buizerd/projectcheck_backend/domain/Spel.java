package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Spel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long spelId;

	@Column(updatable = false)
	private LocalDateTime datum;

	@OneToOne(orphanRemoval = true)
	private Begeleider begeleider;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "spel")
	private Set<Deelnemer> deelnemers;

	public Spel() {
		// An empty constructor is required by JPA

		// Initialize set for compatibility with tests
		this.datum = LocalDateTime.now();
		this.deelnemers = new HashSet<>();
	}

	public void setBegeleider(@NotNull Begeleider begeleider) {
		this.begeleider = begeleider;
	}

	public Long getSpelId() {
		return spelId;
	}

}
