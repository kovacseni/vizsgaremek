package vizsgaremek.consultation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.mentor.MentorDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDto {

    @Schema(description = "Id of the consultation", example = "1")
    private Long id;

    @Schema(description = "Title of the consultation", example = "Konzultáció a vizsgaremekről")
    private String title;

    @Schema(description = "Date and time of the consultation", example = "2021-07-28T15:30:00Z")
    private LocalDateTime time;

    @Schema(description = "Id of the mentor who holds the consultation", example = "2")
    private MentorDto mentor;

    @Schema(description = "Subject of the consultation", example = "Projektmunka és vizsgaremek témák kiosztása")
    private String subject;
}
