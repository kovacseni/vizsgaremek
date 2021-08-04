package vizsgaremek.mentor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/mentors")
@Tag(name = "Management of mentors")
public class MentorController {

    private MentorService service;

    @GetMapping
    @Operation(summary = "Lists all mentors (optional prefix could be given, too)")
    @ApiResponse(responseCode = "200", description = "Mentors have been listed")
    @ApiResponse(responseCode = "400", description = "Bad request, mentors cannot be listed")
    public List<MentorDto> listMentors(@RequestParam Optional<String> prefix) {
        return service.listMentors(prefix);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds an exact mentor by id")
    @ApiResponse(responseCode = "200", description = "Mentor has been found")
    @ApiResponse(responseCode = "400", description = "Bad request, mentor cannot be found")
    @ApiResponse(responseCode = "404", description = "Mentor has not been found")
    public MentorDto findMentorById(@PathVariable("id") long id) {
        return service.findMentorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a mentor")
    @ApiResponse(responseCode = "201", description = "Mentor has been created")
    @ApiResponse(responseCode = "400", description = "Bad request, mentor cannot be created")
    public MentorDto createMentor(@Valid @RequestBody CreateMentorCommand command) {
        return service.createMentor(command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates an exact mentor by id")
    @ApiResponse(responseCode = "200", description = "Mentor has been updated")
    @ApiResponse(responseCode = "400", description = "Bad request, mentor cannot be updated")
    public MentorDto updateMentor(@PathVariable("id") long id, @Valid @RequestBody UpdateMentorCommand command) {
        return service.updateMentor(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Sets the status of an exact mentor to 'DELETED' by id")
    @ApiResponse(responseCode = "204", description = "'DELETED' status has been set on mentor")
    @ApiResponse(responseCode = "400", description = "Bad request, status cannot be set")
    public void deleteMentor(@PathVariable("id") long id) {
        service.deleteMentor(id);
    }
}
