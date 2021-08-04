package vizsgaremek.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    @Query("select c from Consultation c where c.mentor.id = :id")
    Set<Consultation> findConsultationByMentorId(long id);
}
