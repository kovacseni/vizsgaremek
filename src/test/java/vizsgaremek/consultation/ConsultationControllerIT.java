package vizsgaremek.consultation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import vizsgaremek.mentor.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = "delete from consultations")
@Sql(statements = "delete from mentors")
public class ConsultationControllerIT {

    ConsultationDto consultation;
    MentorDto mentor1;
    MentorDto mentor2;

    @Autowired
    TestRestTemplate template;

    @BeforeEach
    void setUp() {
        mentor1 = template.postForObject("/api/mentors",
                new CreateMentorCommand("Mentor Trainer", "trainer.mentor@training.com"),
                MentorDto.class);
        mentor2 = template.postForObject("/api/mentors",
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
        assertThat(expected)
                .extracting(ConsultationDto::getMentor)
                .extracting(MentorDto::getId)
                .containsExactly(mentor1.getId(), mentor1.getId(), mentor1.getId(), mentor2.getId());
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
        assertThat(expected)
                .extracting(ConsultationDto::getMentor)
                .extracting(MentorDto::getName)
                .containsExactly("Mentor Trainer", "Mentor Trainer", "Instructor Mentor");
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
        assertEquals("Mentor Trainer", expected.getMentor().getName());
    }

    @Test
    void testUpdateConsultation() {

        long id = consultation.getId();

        template.put("/api/consultations/" + id, new UpdateConsultationCommand("Remek konzultáció a vizsgaremekről",
                LocalDateTime.of(2021, 8, 4, 15, 30), mentor2.getId()));

        ConsultationDto expected = template.exchange("/api/consultations/" + id,
                HttpMethod.GET,
                null,
                ConsultationDto.class)
                .getBody();

        assertAll(
                () -> assertEquals("Remek konzultáció a vizsgaremekről", expected.getTitle()),
                () -> assertEquals("Instructor Mentor", expected.getMentor().getName()),
                () -> assertEquals("mentor.instructor@training.com", expected.getMentor().getEmail()),
                () -> assertEquals(vizsgaremek.mentor.Status.ACTIVE, expected.getMentor().getStatus()));
    }

    @Test
    void testUpdateConsultationWithNotActiveMentor() {

        long mentor2Id = mentor2.getId();

        template.put("/api/mentors/" + mentor2Id, new UpdateMentorCommand("Mentor Trainer Instructor",
                "mentort@gmail.com", Position.JUNIOR_MENTOR, vizsgaremek.mentor.Status.PENDING));

        long consultationId = consultation.getId();

        template.put("/api/consultations/" + consultationId, new UpdateConsultationCommand("Remek konzultáció a vizsgaremekről",
                LocalDateTime.of(2021, 8, 4, 15, 30), mentor2Id));

        ConsultationDto expected = template.exchange("/api/consultations/" + consultationId,
                HttpMethod.GET,
                null,
                ConsultationDto.class)
                .getBody();

        assertAll(
                () -> assertEquals("Remek konzultáció a vizsgaremekről", expected.getTitle()),
                () -> assertEquals("Mentor Trainer", expected.getMentor().getName()),
                () -> assertEquals("trainer.mentor@training.com", expected.getMentor().getEmail()),
                () -> assertEquals(vizsgaremek.mentor.Status.ACTIVE, expected.getMentor().getStatus()));
    }

    @Test
    void testDeleteConsultation() {

        long id = consultation.getId();

        template.delete("/api/consultations/" + id);

        List<ConsultationDto> expected = template.exchange("/api/consultations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ConsultationDto>>() {
                })
                .getBody();

        assertEquals(3, expected.size());
        assertThat(expected)
                .extracting(ConsultationDto::getTitle)
                .containsExactly("Docker", "Nyitó konzultáció", "Záró konzultáció");
    }

    @Test
    void testListConsultationsByMentorId() {

        long id = mentor1.getId();

        List<ConsultationDto> expected = template.exchange("/api/consultations/mentor/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ConsultationDto>>() {
                })
                .getBody();

        assertEquals(3, expected.size());
        assertThat(expected)
                .extracting(ConsultationDto::getTitle)
                .containsExactly("Konzultáció a vizsgaremekről", "Docker", "Nyitó konzultáció");
    }

    @Test
    void testCreateConsultationWithNullName() {

        Problem expected = template.postForObject("/api/consultations",
                new CreateConsultationCommand(null,
                        LocalDateTime.of(2021, 7, 28, 15, 30),
                        mentor1.getId()),
                Problem.class);

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testCreateConsultationWithEmptyName() {

        Problem expected = template.postForObject("/api/consultations",
                new CreateConsultationCommand("   ",
                        LocalDateTime.of(2021, 7, 28, 15, 30),
                        mentor1.getId()),
                Problem.class);

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testCreateConsultationWithNullTime() {

        Problem expected = template.postForObject("/api/consultations",
                new CreateConsultationCommand("Konzultáció a vizsgaremekről",
                        null,
                        mentor1.getId()),
                Problem.class);

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testCreateConsultationWithNullMentorId() {

        Problem expected = template.postForObject("/api/consultations",
                new CreateConsultationCommand("Konzultáció a vizsgaremekről",
                        LocalDateTime.of(2021, 7, 28, 15, 30),
                        null),
                Problem.class);

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testCreateConsultationWithNotActiveMentor() {

        long mentor2Id = mentor2.getId();

        template.put("/api/mentors/" + mentor2Id, new UpdateMentorCommand("Mentor Trainer Instructor",
                "mentort@gmail.com", Position.JUNIOR_MENTOR, vizsgaremek.mentor.Status.PENDING));

        Problem expected = template.postForObject("/api/consultations",
                new CreateConsultationCommand("Konzultáció a vizsgaremekről",
                        LocalDateTime.of(2021, 7, 28, 15, 30),
                        mentor2Id),
                Problem.class);

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testUpdateConsultationWithNullName() {

        long id = consultation.getId();

        Problem expected = template.exchange("/api/consultations/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateConsultationCommand(null,
                        LocalDateTime.of(2021, 8, 4, 15, 30), mentor2.getId(),
                        "Projektmunka és vizsgaremek témák kiosztása")
                ),
                Problem.class)
                .getBody();

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testUpdateConsultationWithEmptyName() {

        long id = consultation.getId();

        Problem expected = template.exchange("/api/consultations/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateConsultationCommand("   ",
                        LocalDateTime.of(2021, 8, 4, 15, 30), mentor2.getId(),
                        "Projektmunka és vizsgaremek témák kiosztása")
                ),
                Problem.class)
                .getBody();

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testUpdateConsultationWithNullTime() {

        long id = consultation.getId();

        Problem expected = template.exchange("/api/consultations/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateConsultationCommand("Remek konzultáció a vizsgaremekről",
                        null, mentor2.getId(), "Projektmunka és vizsgaremek témák kiosztása")
                ),
                Problem.class)
                .getBody();

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }

    @Test
    void testUpdateConsultationWithNullMentorId() {

        long id = consultation.getId();

        Problem expected = template.exchange("/api/consultations/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateConsultationCommand("Remek konzultáció a vizsgaremekről",
                        LocalDateTime.of(2021, 8, 4, 15, 30), null,
                        "Projektmunka és vizsgaremek témák kiosztása")
                ),
                Problem.class)
                .getBody();

        assertEquals(Status.BAD_REQUEST, expected.getStatus());
    }
}
