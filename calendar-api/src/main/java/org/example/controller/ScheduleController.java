package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.RequestStatus;
import org.example.dto.*;
import org.example.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
public class ScheduleController {

    private final ScheduleQueryService scheduleQueryService;
    private final TaskService taskService;
    private final EventService eventService;
    private final ShareService shareService;
    private final EngagementService engagementService;
    private final NotificationService notificationService;

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @RequestBody TaskCreateRequest taskCreateRequest,
            AuthUser authUser) {
        taskService.create(taskCreateRequest, authUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/events")
    public ResponseEntity<Void> createEvent(
            @RequestBody EventCreateRequest eventCreateRequest,
            AuthUser authUser) {
        eventService.create(eventCreateRequest, authUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications")
    public ResponseEntity<Void> createNotifications(
            @RequestBody NotificationsCreateRequest notificationsCreateRequest,
            AuthUser authUser) {
        notificationService.create(notificationsCreateRequest, authUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/day")
    public List<ScheduleDto> getScheduleByDay(
            AuthUser authUser,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleQueryService.getScheduleByDay(authUser, date == null ? LocalDate.now() : date);
    }

    @GetMapping("/week")
    public List<ScheduleDto> getScheduleByWeek(
            AuthUser authUser,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startOfWeek) {
        return scheduleQueryService.getScheduleByWeek(authUser, startOfWeek == null ? LocalDate.now() : startOfWeek);
    }

    @GetMapping("/month")
    public List<ScheduleDto> getScheduleByMonth(
            AuthUser authUser,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") String yearMonth) {
        return scheduleQueryService.getScheduleByMonth(authUser, yearMonth == null ? YearMonth.now() : YearMonth.parse(yearMonth));
    }

    @PutMapping("/events/engagements/{engagementId}")
    public RequestStatus updateEngagement(
            @Valid @RequestBody ReplyEngagementRequest replyEngagementRequest,
            @PathVariable Long engagementId,
            AuthUser authUser) {
        return engagementService.update(authUser, engagementId, replyEngagementRequest.getType());
    }

    @PostMapping("/shares")
    public void shareSchedule(
            AuthUser authUser,
            @Valid @RequestBody CreateShareRequest request) {
        shareService.createShare(authUser.getId(), request.getToUserId(), request.getDirection());
    }

    @PutMapping("/shares/{shareId}")
    public void replyToShareRequest(
            @PathVariable Long shareId,
            @Valid @RequestBody ReplyRequest replyRequest,
            AuthUser authUser
    ) {
        shareService.replyToShareRequest(shareId, authUser.getId(), replyRequest.getType());
    }
}