package com.example.jwtexample.repository;

import com.example.jwtexample.domain.Issue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @EntityGraph(attributePaths = "project")
    @Query("select i from Issue i")
    List<Issue> findAllWithProject();
}
