package vizsgaremek.consultation;

import lombok.*;
import vizsgaremek.mentor.Mentor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    @ToString.Exclude
    private Mentor mentor;

    private String subject;

    public Consultation(String title, LocalDateTime time, Mentor mentor) {
        this.title = title;
        this.time = time;
        this.mentor = mentor;
    }

    public Consultation(String title, LocalDateTime time, Mentor mentor, String subject) {
        this.title = title;
        this.time = time;
        this.mentor = mentor;
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}
