package com.example.jwtexample.domain;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IssueEnums.Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IssueEnums.Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    protected Issue() { }

    private Issue(String title, String description, IssueEnums.Status status, IssueEnums.Priority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public static Issue of(String title, String description, IssueEnums.Status status, IssueEnums.Priority priority) {
        return new Issue(title, description, status, priority);
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public IssueEnums.Status getStatus() { return status; }
    public IssueEnums.Priority getPriority() { return priority; }
    public Project getProject() { return project; }

    void setProjectInternal(Project project) { this.project = project; }

    public void change(String newTitle, String newDescription, IssueEnums.Status newStatus, IssueEnums.Priority newPriority) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        this.title = newTitle;
        this.description = newDescription;
        this.status = newStatus;
        this.priority = newPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null) { return false; }
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) { return false; }
        Issue other = (Issue) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
