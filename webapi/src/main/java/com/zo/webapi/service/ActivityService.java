package com.zo.webapi.service;

import com.zo.webapi.dto.ActivityCreateRequestDTO;
import com.zo.webapi.dto.ActivityResponseDTO;
import com.zo.webapi.dto.ActivityUpdateRequestDTO;
import com.zo.webapi.exception.InvalidDataException;
import com.zo.webapi.exception.ResourceNotFoundException;
import com.zo.webapi.model.Activity;
import com.zo.webapi.model.Consultant;
import com.zo.webapi.repository.ActivityRepository;
import com.zo.webapi.repository.ConsultantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ConsultantRepository consultantRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter CREATED_FMT = DateTimeFormatter.ISO_OFFSET_DATE_TIME; // ISO-8601

    public ActivityService(ActivityRepository activityRepository, ConsultantRepository consultantRepository) {
        this.activityRepository = activityRepository;
        this.consultantRepository = consultantRepository;
    }

    @Transactional
    public ActivityResponseDTO createActivity(ActivityCreateRequestDTO dto) {

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new ResourceNotFoundException("Consultant", "id", dto.getConsultantId()));

        Activity activity = new Activity();
        activity.setTitle(dto.getTitle());
        activity.setType(dto.getType());
        activity.setConsultant(consultant);
        activity.setDate(parseDate(dto.getDate(), "date"));
        activity.setStartTime(parseTime(dto.getStartTime(), "startTime"));
        activity.setEndTime(parseTime(dto.getEndTime(), "endTime"));
        activity.setDescription(dto.getDescription());

        // validera att startTime < endTime
        validateTimeRange(activity.getStartTime(), activity.getEndTime());

        Activity saved = activityRepository.save(activity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ActivityResponseDTO> getAllActivities(Long consultantId, String from, String to) {
        List<Activity> activities;

        if (from != null && to != null) {
            LocalDate fromDate = parseDate(from, "from");
            LocalDate toDate = parseDate(to, "to");
            if (fromDate.isAfter(toDate)) {
                throw new InvalidDataException("'from' cannot be after 'to'");
            }

            if(consultantId != null) {
                activities = activityRepository.findByConsultantIdAndDateBetweenOrderByDateAscStartTimeAsc(
                        consultantId, fromDate, toDate
                );
            } else {
                activities = activityRepository.findByDateBetweenOrderByDateAscStartTimeAsc(fromDate, toDate);
            }

        } else if (from == null && to == null) {
            if(consultantId != null) {
                activities = activityRepository.findByConsultantIdOrderByDateAscStartTimeAsc(consultantId);
            } else {
                activities = activityRepository.findAllByOrderByDateAscStartTimeAsc();
            }
        } else {
            throw new InvalidDataException("Both 'from' and 'to' must be provided together");
        }

        return activities.stream().map(this::toResponse).toList();
    }

    @Transactional
    public ActivityResponseDTO updateActivity (Long id, ActivityUpdateRequestDTO dto) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));

        if (dto.getTitle() != null) activity.setTitle(dto.getTitle());
        if (dto.getType() != null) activity.setType(dto.getType());
        if (dto.getConsultantId() != null) {
            Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Consultant", "id", dto.getConsultantId()));
            activity.setConsultant(consultant);
        }
        if (dto.getDate() != null) activity.setDate(parseDate(dto.getDate(), "date"));
        if (dto.getStartTime() != null) activity.setStartTime(parseTime(dto.getStartTime(), "startTime"));
        if (dto.getEndTime() != null) activity.setEndTime(parseTime(dto.getEndTime(), "endTime"));
        if (dto.getDescription() != null) activity.setDescription(dto.getDescription());

        validateTimeRange(activity.getStartTime(), activity.getEndTime());

        Activity saved = activityRepository.save(activity);
        return toResponse(saved);
    }

    @Transactional
    public void deleteActivity(Long id) {
        if(!activityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activity", "id", id);
        }
        activityRepository.deleteById(id);
    }

    // Helpers

    private ActivityResponseDTO toResponse(Activity a) {
        return new ActivityResponseDTO(
                a.getId(),
                a.getTitle(),
                a.getType(),
                a.getConsultant() != null ? a.getConsultant().getId() : null,
                a.getDate() != null ? a.getDate().format(DATE_FMT) : null,
                a.getStartTime() != null ? a.getStartTime().format(TIME_FMT) : null,
                a.getEndTime() != null ? a.getEndTime().format(TIME_FMT) : null,
                a.getDescription(),
                formatCreatedAt(a.getCreatedAt())
        );
    }

    /***
     * Entitetsklassen använder LocalDate, LocalTime och OffsetDateTime
     * Frontend vill ha det som Strings
     * */
    private String formatCreatedAt(OffsetDateTime createdAt) {
        return createdAt != null ? createdAt.format(CREATED_FMT) : null;
    }

    private LocalDate parseDate(String value, String field) {
        try {
            return LocalDate.parse(value, DATE_FMT);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException("Invalid " + field + " format (expected YYYY-MM-DD)");
        }
    }


    private LocalTime parseTime (String value, String field) {
        try {
            return LocalTime.parse(value, TIME_FMT);
        } catch (DateTimeParseException e) {
            throw new InvalidDataException("Invalid " + field + " format (expected HH:mm)");
        }
    }

    // Validera att startTime är innan endTime
    private void validateTimeRange(LocalTime start, LocalTime end) {
        if (start != null && end != null && !start.isBefore(end)) {
            throw new InvalidDataException("startTime must be before endTime");
        }
    }
}
