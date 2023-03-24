package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.Schedule;
import org.example.core.service.UserService;
import org.example.dto.AuthUser;
import org.example.dto.TaskCreateRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserService userService;

    public void create(TaskCreateRequest taskCreateRequest, AuthUser authUser) {
        final Schedule taskSchedule =
                Schedule.task(taskCreateRequest.getTitle(),
                        taskCreateRequest.getDescription(),
                        taskCreateRequest.getTaskAt(),
                        userService.findByUserId(authUser.getId()));
    }
}
