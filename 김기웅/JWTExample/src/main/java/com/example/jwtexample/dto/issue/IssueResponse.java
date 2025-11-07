package com.example.jwtexample.dto.issue;

import com.example.jwtexample.domain.Issue;

public final class IssueResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final String priority;
    private final Long projectId;
    private final String projectKey;

    public IssueResponse(Long id, String title, String description,
                         String status, String priority, Long projectId, String projectKey) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.projectId = projectId;
        this.projectKey = projectKey;
    }

    public static IssueResponse from(Issue i) {
        final var p = i.getProject();
        return new IssueResponse(
                i.getId(),
                i.getTitle(),
                i.getDescription(),
                i.getStatus().name(),
                i.getPriority().name(),
                p.getId(),
                p.getKeyCode()
        );
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public Long getProjectId() { return projectId; }
    public String getProjectKey() { return projectKey; }
}
