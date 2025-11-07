package com.example.jwtexample.dto.project;

import com.example.jwtexample.domain.Project;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public final class ProjectResponse {

    private final Long id;
    private final String name;
    private final String keyCode;
    private final LocalDateTime createdAt;

    public ProjectResponse(Long id, String name, String keyCode, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.keyCode = keyCode;
        this.createdAt = createdAt;
    }

    public static ProjectResponse from(Project p) {
        return new ProjectResponse(p.getId(), p.getName(), p.getKeyCode(), p.getCreatedAt());
    }
}
