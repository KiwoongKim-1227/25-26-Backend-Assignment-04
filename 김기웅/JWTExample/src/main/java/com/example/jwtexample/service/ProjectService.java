package com.example.jwtexample.service;

import com.example.jwtexample.common.exception.NotFoundException;
import com.example.jwtexample.domain.Project;
import com.example.jwtexample.dto.project.ProjectCreateRequest;
import com.example.jwtexample.dto.project.ProjectResponse;
import com.example.jwtexample.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponse create(ProjectCreateRequest request) {
        if (projectRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 프로젝트명입니다: " + request.getName());
        }
        if (projectRepository.existsByKeyCode(request.getKeyCode())) {
            throw new IllegalArgumentException("이미 존재하는 프로젝트 키입니다: " + request.getKeyCode());
        }
        Project saved = projectRepository.save(Project.of(request.getName(), request.getKeyCode()));
        return ProjectResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream()
                .map(ProjectResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Project getEntity(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. id=" + id));
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id) {
        return ProjectResponse.from(getEntity(id));
    }

    @Transactional
    public void delete(Long id) {
        Project project = getEntity(id);
        if (!project.getIssues().isEmpty()) {
            throw new IllegalStateException("이슈가 존재하는 프로젝트는 삭제할 수 없습니다.");
        }
        projectRepository.delete(project);
    }
}
