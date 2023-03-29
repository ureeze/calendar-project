package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.Schedule;
import org.example.core.domain.entity.User;
import org.example.core.domain.entity.repository.ScheduleRepository;
import org.example.core.service.UserService;
import org.example.dto.AuthUser;
import org.example.dto.NotificationsCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserService userService;
    private final ScheduleRepository scheduleRepository;


    @Transactional
    public void create(NotificationsCreateRequest notificationsCreateRequest, AuthUser authUser) {
        final User user = userService.findByUserId(authUser.getId());
        final List<LocalDateTime> notifyAtList = notificationsCreateRequest.getRepeatTimes();
        notifyAtList.forEach(notifyAt -> {
            final Schedule notificationSchedule =
                    Schedule.notification(
                            notificationsCreateRequest.getTitle(),
                            notifyAt,
                            user);
            scheduleRepository.save(notificationSchedule);
        });
    }
}
