package com.example.jwtexample.repository;


import com.example.jwtexample.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);
    boolean existsByKeyCode(String keyCode);
}
