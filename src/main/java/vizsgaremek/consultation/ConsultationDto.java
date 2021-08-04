package vizsgaremek.consultation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.mentor.MentorDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {

    private Long id;
    private String title;
    private LocalDateTime time;
    private MentorDto mentor;
    private String subject;
}
