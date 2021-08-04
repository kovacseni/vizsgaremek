package vizsgaremek.consultation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import vizsgaremek.mentor.CreateMentorCommand;
import vizsgaremek.mentor.MentorDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "delete from consultations")
public class ConsultationControllerIT {

    ConsultationDto consultation;

    @Autowired
    TestRestTemplate template;

    @BeforeEach
    void setUp() {
        MentorDto mentor1 = template.postForObject("/api/mentors",
                new CreateMentorCommand("Mentor Trainer", "trainer.mentor@training.com"),
                MentorDto.class);
        MentorDto mentor2 = template.postForObject("/api/mentors",
                new CreateMentorCommand("Instructor Mentor", "mentor.instructor@training.com"),
                MentorDto.class);

        long mentor1Id = mentor1.getId();
        long mentor2Id = mentor2.getId();

        consultation = template.postForObject("/api/consultations",
                new CreateConsultationCommand("Konzultáció a vizsgaremekről",
                        LocalDateTime.of(2021, 7, 28, 15, 30),
                        mentor1Id),
                ConsultationDto.class);
        template.postForObject("/api/consultations",
                new CreateConsultationCommand("Docker",
                        LocalDateTime.of(2021, 6, 23, 15, 30),
                        mentor1Id),
                ConsultationDto.class);
        template.postForObject("/api/consultations",
                new CreateConsultationCommand("Nyitó konzultáció",
                        LocalDateTime.of(2021, 5, 31, 15, 30),
                        mentor1Id),
                ConsultationDto.class);
        template.postForObject("/api/consultations",
                new CreateConsultationCommand("Záró konzultáció",
                        LocalDateTime.of(2021, 8, 6, 15, 30),
                        mentor2Id),
                ConsultationDto.class);
    }

    @Test
    void testListConsultations() {

        List<ConsultationDto> expected = template.exchange("/api/consultations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ConsultationDto>>() {
                })
                .getBody();

        assertEquals(4, expected.size());
        assertThat(expected)
                .extracting(ConsultationDto::getTitle)
                .containsExactly("Konzultáció a vizsgaremekről", "Docker", "Nyitó konzultáció", "Záró konzultáció");
    }

    @Test
    void testListConsultationsByPrefix() {

        List<ConsultationDto> expected = template.exchange("/api/consultations?prefix=konz",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ConsultationDto>>() {
                })
                .getBody();

        assertEquals(3, expected.size());
        assertThat(expected)
                .extracting(ConsultationDto::getTitle)
                .containsExactly("Konzultáció a vizsgaremekről", "Nyitó konzultáció", "Záró konzultáció");
    }

    @Test
    void testFindConsultationById() {

        long id = consultation.getId();

        ConsultationDto expected = template.exchange("/api/consultations/" + id,
                HttpMethod.GET,
                null,
                ConsultationDto.class)
                .getBody();

        assertEquals("Konzultáció a vizsgaremekről", expected.getTitle());
    }


}
