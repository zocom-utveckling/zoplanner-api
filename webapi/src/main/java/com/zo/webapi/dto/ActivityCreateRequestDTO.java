package com.zo.webapi.dto;

import com.zo.webapi.enums.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ActivityCreateRequestDTO {

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "type is required")
    private ActivityType type;

    @NotBlank(message = "date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "date must be YYYY-MM-DD")
    private String date; // "yyyy-MM-dd"

    @NotBlank(message = "startTime is required")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "time must be HH:mm")
    private String startTime; // "HH:mm"

    @NotBlank(message = "endTime is required")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "time must be HH:mm")
    private String endTime; // "HH:mm"

    private String description;

    public ActivityCreateRequestDTO() {}

    public ActivityCreateRequestDTO(String title, ActivityType type, String date,
                                    String startTime, String endTime, String description) {
        this.title = title;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
