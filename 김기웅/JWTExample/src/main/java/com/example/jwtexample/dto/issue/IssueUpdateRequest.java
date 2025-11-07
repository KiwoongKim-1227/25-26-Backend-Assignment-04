package com.example.jwtexample.dto.issue;

import com.example.jwtexample.domain.IssueEnums.Priority;
import com.example.jwtexample.domain.IssueEnums.Status;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class IssueUpdateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    private final String description;

    @NotNull
    private final Status status;

    @NotNull
    private final Priority priority;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IssueUpdateRequest(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("status") Status status,
            @JsonProperty("priority") Priority priority
    ) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public Priority getPriority() { return priority; }
}
