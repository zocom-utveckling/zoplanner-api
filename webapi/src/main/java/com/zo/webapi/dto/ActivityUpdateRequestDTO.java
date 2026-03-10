package com.zo.webapi.dto;

import com.zo.webapi.enums.ActivityType;
import jakarta.validation.constraints.Pattern;

public class ActivityUpdateRequestDTO {

    @Pattern(regexp = ".*\\S.*", message = "title must not be blank")
    private String title;

    private ActivityType type;
    private Long consultantId;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "date must be YYYY-MM-DD")
    private String date;

    @Pattern(regexp = "\\d{2}:\\d{2}", message = "time must be HH:mm")
    private String startTime;

    @Pattern(regexp = "\\d{2}:\\d{2}", message = "time must be HH:mm")
    private String endTime;

    private String description;

    public ActivityUpdateRequestDTO() {}

    public ActivityUpdateRequestDTO(String title, ActivityType type, Long consultantId, String date,
                                    String startTime, String endTime, String description) {
        this.title = title;
        this.type = type;
        this.consultantId = consultantId;
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

    public Long getConsultantId() {return consultantId;}

    public void setConsultantId(Long consultantId) { this.consultantId = consultantId;}

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
