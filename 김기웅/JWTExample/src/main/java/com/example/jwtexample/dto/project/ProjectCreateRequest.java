package com.example.jwtexample.dto.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public final class ProjectCreateRequest {

    @NotBlank(message = "프로젝트명은 필수입니다.")
    private final String name;

    @NotBlank(message = "프로젝트 키는 필수입니다.")
    @Pattern(regexp = "^[A-Z]{2,5}$", message = "키는 대문자 2~5자여야 합니다.")
    private final String keyCode;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProjectCreateRequest(
            @JsonProperty("name") String name,
            @JsonProperty("keyCode") String keyCode
    ) {
        this.name = name;
        this.keyCode = keyCode;
    }

    public String getName() {
        return name;
    }

    public String getKeyCode() {
        return keyCode;
    }
}
