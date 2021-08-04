package vizsgaremek.consultation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private ConsultationService service;

    @GetMapping
    public List<ConsultationDto> listConsultations(@RequestParam Optional<String> prefix) {
        return service.listConsultations(prefix);
    }

    @GetMapping("/{id}")
    public ConsultationDto findConsultationById(@PathVariable("id") long id) {
        return service.findConsultationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationDto createAndAddConsultation(@Valid @RequestBody CreateConsultationCommand command) {
        return service.createAndAddConsultation(command);
    }

    @PutMapping("/{id}")
    public ConsultationDto updateConsultation(@PathVariable("id") long id, @Valid @RequestBody UpdateConsultationCommand command) {
        return service.updateConsultation(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsultation(@PathVariable("id") long id) {
        service.deleteConsultation(id);
    }

    @GetMapping("/mentor/{id}")
    public List<ConsultationDto> listConsultationsByMentorId(@PathVariable("id") long id, @RequestParam Optional<String> prefix) {
        return service.listConsultationsByMentorId(id, prefix);
    }
}
