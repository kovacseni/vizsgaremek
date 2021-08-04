package vizsgaremek.consultation;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vizsgaremek.mentor.Mentor;
import vizsgaremek.mentor.MentorRepository;
import vizsgaremek.mentor.Status;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ConsultationService {

    private ModelMapper modelMapper;

    private ConsultationRepository repository;

    @Autowired
    private MentorRepository mentorRepository;

    public List<ConsultationDto> listConsultations(Optional<String> prefix) {
        Type targetListType = new TypeToken<List<ConsultationDto>>() {
        }.getType();
        List<Consultation> filteredConsultations = repository.findAll().stream()
                .filter(consultation -> prefix.isEmpty()
                        || consultation.getTitle().toLowerCase().contains(prefix.get().toLowerCase()))
                .collect(Collectors.toList());
        return modelMapper.map(filteredConsultations, targetListType);
    }

    public ConsultationDto findConsultationById(long id) {
        Consultation consultation = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consultation with id: " + id + " not found."));
        return modelMapper.map(consultation, ConsultationDto.class);
    }

    @Transactional
    public ConsultationDto createAndAddConsultation(CreateConsultationCommand command) {
        Mentor mentor = mentorRepository.findById(command.getMentorId())
                .orElseThrow(() -> new IllegalArgumentException("Mentor with id: " + command.getMentorId() + " not found."));
        if (mentor.getStatus() == Status.ACTIVE) {
            Consultation consultation = makeNewConsultation(command, mentor);
            repository.save(consultation);

            return modelMapper.map(consultation, ConsultationDto.class);
        }
        throw new IllegalArgumentException("Mentor with id: " + command.getMentorId() + " is not in active status.");
    }

    private Consultation makeNewConsultation(CreateConsultationCommand command, Mentor mentor) {
        Consultation consultation = new Consultation(command.getTitle(), command.getTime(), mentor);
        if (command.getSubject() != null) {
            consultation.setSubject(command.getSubject());
        }
        return consultation;
    }

    @Transactional
    public ConsultationDto updateConsultation(long id, UpdateConsultationCommand command) {
        Mentor mentor = mentorRepository.findById(command.getMentorId())
                .orElseThrow(() -> new IllegalArgumentException("Mentor with id: " + command.getMentorId() + " not found."));
        Consultation consultation = getConsultationAndSetValues(id, command, mentor);

        return modelMapper.map(consultation, ConsultationDto.class);
    }

    private Consultation getConsultationAndSetValues(long id, UpdateConsultationCommand command, Mentor mentor) {
        Consultation consultation = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consultation with id: " + id + " not found."));

        consultation.setTitle(command.getTitle());
        consultation.setTime(command.getTime());
        if (mentor.getStatus() == Status.ACTIVE) {
            consultation.setMentor(mentor);
        }
        if (command.getSubject() != null) {
            consultation.setSubject(command.getSubject());
        }

        return consultation;
    }

    public void deleteConsultation(long id) {
        repository.deleteById(id);
    }

    public List<ConsultationDto> listConsultationsByMentorId(long id, Optional<String> prefix) {
        Type targetType = new TypeToken<List<ConsultationDto>>() {
        }.getType();
        List<Consultation> consultations = repository.findConsultationByMentorId(id).stream()
                .filter(consultation -> prefix.isEmpty()
                        || consultation.getTitle().toLowerCase().contains(prefix.get().toLowerCase()))
                .collect(Collectors.toList());

        return modelMapper.map(consultations, targetType);
    }
}
