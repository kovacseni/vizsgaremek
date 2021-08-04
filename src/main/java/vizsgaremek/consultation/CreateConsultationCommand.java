package vizsgaremek.consultation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsultationCommand {

    @NotNull
    @NotBlank
    @Schema(description = "Title of the consultation", example = "Konzultáció a vizsgaremekről")
    private String title;

    @NotNull
    @Schema(description = "Date and time of the consultation", example = "2021-07-28T15:30:00Z")
    private LocalDateTime time;

    @NotNull
    @Schema(description = "Id of the mentor who holds the consultation", example = "2")
    private Long mentorId;

    @Schema(description = "Subject of the consultation", example = "Projektmunka és vizsgaremek témák kiosztása")
    private String subject;

    public CreateConsultationCommand(String title, LocalDateTime time, Long mentorId) {
        this.title = title;
        this.time = time;
        this.mentorId = mentorId;
    }
}
