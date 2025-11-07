package com.example.jwtexample.dto.issue;

import com.example.jwtexample.domain.IssueEnums.Priority;
import com.example.jwtexample.domain.IssueEnums.Status;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class IssueCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    private final String description;

    @NotNull(message = "상태는 필수입니다.")
    private final Status status;

    @NotNull(message = "우선순위는 필수입니다.")
    private final Priority priority;

    @NotNull(message = "프로젝트 ID는 필수입니다.")
    private final Long projectId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IssueCreateRequest(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("status") Status status,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("projectId") Long projectId
    ) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.projectId = projectId;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public Long getProjectId() { return projectId; }
}
