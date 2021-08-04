package vizsgaremek.mentor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMentorCommand {

    @NotNull
    @NotBlank
    @Schema(description = "Name of the mentor", example = "Mentor Trainer")
    private String name;

    @NotNull
    @NotBlank
    @Schema(description = "E-mail address of the mentor", example = "trainer.mentor@training.com")
    private String email;
}
