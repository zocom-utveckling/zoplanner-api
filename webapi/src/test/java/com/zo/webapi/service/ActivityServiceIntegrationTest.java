package com.zo.webapi.service;

import com.zo.webapi.WebapiApplication;
import com.zo.webapi.dto.ActivityCreateRequestDTO;
import com.zo.webapi.dto.ActivityResponseDTO;
import com.zo.webapi.dto.ActivityUpdateRequestDTO;
import com.zo.webapi.enums.ActivityType;
import com.zo.webapi.enums.UserRole;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Activity;
import com.zo.webapi.model.User;
import com.zo.webapi.repository.ActivityRepository;
import com.zo.webapi.repository.UserRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = WebapiApplication.class)
@Transactional
public class ActivityServiceIntegrationTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        activityRepository.deleteAll();
        userRepository.deleteAll();

        user1 = user("user1", "user1@test.com");
        user2 = user("user2", "user2@test.com");

        // Databasseed för sortering & intervall
        activityRepository.save(activity("B", ActivityType.MEETING,
                LocalDate.of(2026, 2, 10), LocalTime.of(9, 0),
                LocalTime.of(10, 0), "Beskrivning B", user1));

        activityRepository.save(activity("A", ActivityType.LESSON,
                LocalDate.of(2026, 2, 9), LocalTime.of(8, 0),
                LocalTime.of(9, 0), null, user1));

        activityRepository.save(activity("C", ActivityType.REVIEW,
                LocalDate.of(2026, 2, 10), LocalTime.of(8, 30),
                LocalTime.of(9, 0), "Beskrivning C", user2));
    }

    @Test
    void createActivity_Success() {
        ActivityCreateRequestDTO dto = new ActivityCreateRequestDTO(
                "Ny aktivitet",
                ActivityType.OTHER,
                user1.getId(),
                "2026-02-11",
                "13:00",
                "14:00",
                "Beskrivning"
        );

        ActivityResponseDTO created = activityService.createActivity(dto);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Ny aktivitet");
        assertThat(created.getType()).isEqualTo(ActivityType.OTHER);
        assertThat(created.getUserId()).isEqualTo(user1.getId());
        assertThat(created.getDate()).isEqualTo("2026-02-11");
        assertThat(created.getStartTime()).isEqualTo("13:00");
        assertThat(created.getEndTime()).isEqualTo("14:00");
        assertThat(created.getDescription()).isEqualTo("Beskrivning");
        assertThat(created.getCreatedAt()).isNotBlank();

        Activity saved = activityRepository.findById(created.getId()).orElseThrow();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getDate()).isEqualTo(LocalDate.of(2026, 2, 11));
        assertThat(saved.getStartTime()).isEqualTo(LocalTime.of(13, 0));
        assertThat(saved.getUser().getId()).isEqualTo(user1.getId());
    }

    @Test
    void getAllActivities_noParams_returnsSorted() {
        List<ActivityResponseDTO> res = activityService.getAllActivities(
                null, null, null);

        assertThat(res).hasSize(3);

        // Ska sortera efter dag, sedan startTime om samma dag
        assertThat(res)
                .extracting(ActivityResponseDTO::getTitle)
                .containsExactly("A", "C", "B");
    }

    @Test
    void getAllActivities_withInterval_returnsSorted() {
        List<ActivityResponseDTO> res = activityService.getAllActivities(
                null,"2026-02-10", "2026-02-10");

        assertThat(res).hasSize(2); // Endast två aktiviteter mellan datum, aktivitet "A" annat datum
        assertThat(res)
                .extracting(ActivityResponseDTO::getTitle)
                .containsExactly("C", "B"); // 08:30 före 09:00
    }

    @Test
    void getAllActivities_withUserId_returnsThatUsersActivities() {
        List<ActivityResponseDTO> res = activityService.getAllActivities(
                user1.getId(), null, null);

        assertThat(res).hasSize(2); // user1 har activity A och B
        assertThat(res)
                .extracting(ActivityResponseDTO::getUserId)
                .allMatch(id -> id.equals(user1.getId()));
    }

    @Test
    void getAllActivities_withUserIdAndInterval_returnsFiltered() {
        List<ActivityResponseDTO> res = activityService.getAllActivities(
                user1.getId(), "2026-02-10", "2026-02-10");

        // Endast aktivitet B ska matcha
        assertThat(res).hasSize(1);
        assertThat(res.getFirst().getTitle()).isEqualTo("B");
        assertThat(res.getFirst().getUserId()).isEqualTo(user1.getId());
    }

    @Test
    void updateActivity_Success() {
        Activity existing = activityRepository.findAll().stream()
                .filter(a -> a.getTitle().equals("B"))
                .findFirst()
                .orElseThrow();

        ActivityUpdateRequestDTO patch = new ActivityUpdateRequestDTO();
        patch.setTitle("Uppdaterad");
        patch.setUserId(user2.getId());
        patch.setStartTime("10:00");
        patch.setEndTime("11:00");

        ActivityResponseDTO updated = activityService.updateActivity(existing.getId(), patch);

        assertThat(updated.getId()).isEqualTo(existing.getId());
        assertThat(updated.getTitle()).isEqualTo("Uppdaterad");
        assertThat(updated.getUserId()).isEqualTo(user2.getId());
        assertThat(updated.getStartTime()).isEqualTo("10:00");
        assertThat(updated.getEndTime()).isEqualTo("11:00");

        Activity saved = activityRepository.findById(existing.getId()).orElseThrow();
        assertThat(saved.getTitle()).isEqualTo("Uppdaterad");
        assertThat(saved.getUser().getId()).isEqualTo(user2.getId());
        assertThat(saved.getStartTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(saved.getEndTime()).isEqualTo(LocalTime.of(11, 0));
    }

    @Test
    void deleteActivity_Success() {
        Long id = activityRepository.findAll().get(0).getId();

        activityService.deleteActivity(id);

        assertThat(activityRepository.existsById(id)).isFalse();
    }


    // Helper

    private User user(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setName("Test User");
        user.setEmail(email);
        user.setCity("Stockholm");
        user.setRole(UserRole.CONSULTANT);
        user = userRepository.save(user);

        return userRepository.save(user);

    }

    private Activity activity(String title, ActivityType type, LocalDate date,
                              LocalTime start, LocalTime end, String description, User user) {
        Activity a = new Activity();
        a.setTitle(title);
        a.setType(type);
        a.setDate(date);
        a.setStartTime(start);
        a.setEndTime(end);
        a.setDescription(description);
        a.setUser(user);
        // createdAt sätts av @PrePersist i entitetsklassen
        return a;
    }
}
