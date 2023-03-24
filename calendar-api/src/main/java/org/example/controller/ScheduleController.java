package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AuthUser;
import org.example.dto.TaskCreateRequest;
import org.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static org.example.service.LoginService.LOGIN_SESSION_KEY;

@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
public class ScheduleController {
    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @RequestBody TaskCreateRequest taskCreateRequest,
            HttpSession session,
            AuthUser authUser) {
//        final Long userId = (Long) session.getAttribute(LOGIN_SESSION_KEY);
//        if (userId == null) {
//            throw new RuntimeException("bad request, no session");
//        }
        taskService.create(taskCreateRequest, authUser);
        return ResponseEntity.ok().build();
    }
}
