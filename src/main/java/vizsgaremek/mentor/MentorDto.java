package vizsgaremek.mentor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.consultation.ConsultationDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentorDto {

    @Schema(description = "Id of the mentor", example = "2")
    private Long id;

    @Schema(description = "Name of the mentor", example = "Mentor Trainer")
    private String name;

    @Schema(description = "E-mail address of the mentor", example = "trainer.mentor@training.com")
    private String email;

    @Schema(description = "Position of the mentor", example = "JUNIOR_MENTOR")
    private Position position;

    @Schema(description = "Status of the mentor", example = "ACTIVE")
    private Status status;

    @JsonBackReference
    @Schema(description = "Consultations that this mentor holds")
    private List<ConsultationDto> consultations;
}
