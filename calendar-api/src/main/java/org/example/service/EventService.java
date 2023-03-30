package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.RequestStatus;
import org.example.core.domain.entity.Engagement;
import org.example.core.domain.entity.Schedule;
import org.example.core.domain.entity.User;
import org.example.core.domain.entity.repository.EngagementRepository;
import org.example.core.domain.entity.repository.ScheduleRepository;
import org.example.core.exception.CalendarException;
import org.example.core.exception.ErrorCode;
import org.example.core.service.UserService;
import org.example.dto.AuthUser;
import org.example.dto.EngagementEmailStuff;
import org.example.dto.EventCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EmailService emailService;
    private final EngagementRepository engagementRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    @Transactional
    public void create(EventCreateRequest eventCreateRequest, AuthUser authUser) {
        // 이벤트 참여자의 다른 이벤트와 중복이 되면 안된다.
        // 1시-2시 까지 회의가 있는데, 추가로 다른 회의를 등록할 수 없다.
        // 추가로 이메일 발송.
        final List<Engagement> engagementList = engagementRepository.findAll(); // TO DO findAll() 개선

        if (engagementList.stream()
                .anyMatch(e ->
                        eventCreateRequest.getAttendeeIds().contains(e.getAttendee().getId())
                                && e.getRequestStatus() == RequestStatus.ACCEPTED
                                && e.getEvent().isOverlapped(eventCreateRequest.getStartAt(), eventCreateRequest.getEndAt()))) {
            throw new CalendarException(ErrorCode.EVENT_CREATE_OVERLAPPED_PERIOD);
        }

        final Schedule eventSchedule = Schedule.event(
                eventCreateRequest.getTitle(),
                eventCreateRequest.getDescription(),
                eventCreateRequest.getStartAt(),
                eventCreateRequest.getEndAt(),
                userService.findByUserId(authUser.getId())
        );

        scheduleRepository.save(eventSchedule);

        final List<User> attendees =
                eventCreateRequest.getAttendeeIds()
                        .stream().map(id -> userService.findByUserId(id))
                        .collect(Collectors.toList());


        attendees.forEach(attendee -> {

            final Engagement engagement = Engagement.builder()
                    .schedule(eventSchedule)
                    .requestStatus(RequestStatus.REQUESTED)
                    .attendee(attendee)
                    .build();

            engagementRepository.save(engagement);

            emailService.sendEngagement(
                    EngagementEmailStuff.builder()
                            .engagementId(engagement.getId())
                            .title(engagement.getEvent().getTitle())
                            .toEmail(engagement.getAttendee().getEmail())
                            .attendeeEmails(attendees.stream()
                                    .map(User::getEmail)
                                    .collect(Collectors.toList()))
                            .period(engagement.getEvent().getPeriod())
                            .build());
        });
    }
}
