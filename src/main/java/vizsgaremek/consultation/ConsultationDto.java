package vizsgaremek.consultation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.mentor.Mentor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {

    private Long id;
    private String title;
    private LocalDateTime time;
    private Mentor mentor;
    private String subject;
}
