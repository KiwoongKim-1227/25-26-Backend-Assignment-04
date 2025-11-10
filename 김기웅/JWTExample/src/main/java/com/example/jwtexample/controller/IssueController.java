package com.example.jwtexample.controller;

import com.example.jwtexample.dto.issue.IssueCreateRequest;
import com.example.jwtexample.dto.issue.IssueResponse;
import com.example.jwtexample.dto.issue.IssueUpdateRequest;
import com.example.jwtexample.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IssueController {

    private final IssueService issueService;

    @PostMapping("/projects/{projectId}/issues")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IssueResponse> createUnderProject(@PathVariable Long projectId,
                                                            @Valid @RequestBody IssueCreateRequest request) {
        IssueResponse response = issueService.createUnderProject(projectId, request);
        return ResponseEntity.created(URI.create("/api/issues/" + response.getId()))
                .body(response);
    }

    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.findById(id));
    }

    @GetMapping("/issues")
    public ResponseEntity<List<IssueResponse>> list() {
        return ResponseEntity.ok(issueService.findAll());
    }

    @PutMapping("/issues/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IssueResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody IssueUpdateRequest request) {
        return ResponseEntity.ok(issueService.update(id, request));
    }

    @DeleteMapping("/issues/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        issueService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
