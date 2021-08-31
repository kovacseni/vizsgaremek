package vizsgaremek.consultation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consultations")
@Tag(name = "Management of consultations")
public class ConsultationController {

    private ConsultationService service;

    @GetMapping
    @Operation(summary = "Lists all consultations (optional prefix could be given, too)")
    @ApiResponse(responseCode = "200", description = "Consultations have been listed")
    @ApiResponse(responseCode = "400", description = "Bad request, consultations cannot be listed")
    public List<ConsultationDto> listConsultations(@RequestParam Optional<String> prefix) {
        return service.listConsultations(prefix);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds an exact consultation by id")
    @ApiResponse(responseCode = "200", description = "Consultation has been found")
    @ApiResponse(responseCode = "400", description = "Bad request, consultation cannot be found")
    @ApiResponse(responseCode = "404", description = "Consultation has not been found")
    public ConsultationDto findConsultationById(@PathVariable("id") long id) {
        return service.findConsultationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a consultation and adds it to a mentor")
    @ApiResponse(responseCode = "201", description = "Consultation has been created and added to a mentor")
    @ApiResponse(responseCode = "400", description = "Bad request, consultation cannot be created")
    public ConsultationDto createAndAddConsultation(@Valid @RequestBody CreateConsultationCommand command) {
        return service.createAndAddConsultation(command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates an exact consultation by id")
    @ApiResponse(responseCode = "200", description = "Consultation has been updated")
    @ApiResponse(responseCode = "400", description = "Bad request, consultation cannot be updated")
    public ConsultationDto updateConsultation(@PathVariable("id") long id, @Valid @RequestBody UpdateConsultationCommand command) {
        return service.updateConsultation(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes an exact consultation by id")
    @ApiResponse(responseCode = "204", description = "Consultation has been deleted")
    @ApiResponse(responseCode = "400", description = "Bad request, consultation cannot be deleted")
    public void deleteConsultation(@PathVariable("id") long id) {
        service.deleteConsultation(id);
    }

    @GetMapping("/mentor/{id}")
    @Operation(summary = "Lists consultations held by an exact mentor by id of the mentor")
    @ApiResponse(responseCode = "200", description = "Consultations of the mentor have been listed")
    @ApiResponse(responseCode = "400", description = "Bad request, consultations of the mentor cannot be listed")
    public List<ConsultationDto> listConsultationsByMentorId(@PathVariable("id") long id, @RequestParam Optional<String> prefix) {
        return service.listConsultationsByMentorId(id, prefix);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleBadRequest(IllegalArgumentException iae) {
        Problem problem =
                Problem.builder()
                        .withType(URI.create("consultations/mentor/not-active"))
                        .withTitle("Not active")
                        .withStatus(Status.BAD_REQUEST)
                        .withDetail(iae.getMessage())
                        .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
