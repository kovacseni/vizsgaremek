package vizsgaremek.mentor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import vizsgaremek.consultation.Consultation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mentors")
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mentor_name", nullable = false, length = 255)
    private String name;

    @Column(name = "mentor_email", nullable = false, length = 255)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Position position;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "mentor")
    private List<Consultation> consultations;

    public Mentor(String name, String email, Status status) {
        this.name = name;
        this.email = email;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Mentor{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
