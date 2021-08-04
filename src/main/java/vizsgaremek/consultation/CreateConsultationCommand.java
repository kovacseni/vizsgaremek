package vizsgaremek.consultation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.mentor.Mentor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsultationCommand {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private Long mentorId;

    private String subject;

    public CreateConsultationCommand(String title, LocalDateTime time, Long mentorId) {
        this.title = title;
        this.time = time;
        this.mentorId = mentorId;
    }
}
