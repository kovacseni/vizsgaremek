package vizsgaremek.mentor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.consultation.ConsultationDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDto {

    private Long id;
    private String name;
    private String email;
    private Position position;
    private Status status;
    @JsonBackReference
    private List<ConsultationDto> consultations;
}
