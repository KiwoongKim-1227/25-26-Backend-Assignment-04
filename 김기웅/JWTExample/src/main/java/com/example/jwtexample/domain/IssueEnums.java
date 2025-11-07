package com.example.jwtexample.domain;

public final class IssueEnums {
    private IssueEnums() {}

    public enum Status { OPEN, IN_PROGRESS, RESOLVED, CLOSED }
    public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
}
