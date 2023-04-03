package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.repository.EngagementRepository;
import org.example.core.domain.entity.repository.ScheduleRepository;
import org.example.core.service.UserService;
import org.example.core.util.Period;
import org.example.dto.AuthUser;
import org.example.dto.ScheduleDto;
import org.example.dto.SharedScheduleDto;
import org.example.util.DtoConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleQueryService {

    private final ShareService shareService;
    private final UserService userService;
    private final ScheduleRepository scheduleRepository;
    private final EngagementRepository engagementRepository;

    public List<ScheduleDto> getScheduleByDay(AuthUser authUser, LocalDate date) {
        return getScheduleDtos(authUser,
                Period.of(date, date));

    }

    public List<ScheduleDto> getScheduleByWeek(AuthUser authUser, LocalDate startOfWeek) {
        return getScheduleDtos(authUser,
                Period.of(startOfWeek, startOfWeek.plusDays(6)));
    }


    public List<ScheduleDto> getScheduleByMonth(AuthUser authUser, YearMonth yearMonth) {
        return getScheduleDtos(authUser,
                Period.of(yearMonth.atDay(1), yearMonth.atEndOfMonth()));
    }

    private List<ScheduleDto> getScheduleDtos(AuthUser authUser, Period period) {
        final Stream<ScheduleDto> schedules =
                scheduleRepository.findAllByWriter_Id(authUser.getId())
                        .stream()
                        .filter(schedule -> schedule.isOverlapped(period))
                        .map(schedule -> DtoConverter.fromSchedule(schedule));
        final Stream<ScheduleDto> engagements =
                engagementRepository.findAllByAttendee_Id(authUser.getId())
                        .stream()
                        .filter(engagement -> engagement.isOverlapped(period))
                        .map(engagement -> DtoConverter.fromSchedule(engagement.getSchedule()));
        return Stream.concat(schedules, engagements).collect(Collectors.toList());
    }

    public List<SharedScheduleDto> getSharedScheduleByDay(AuthUser authUser, LocalDate date) {
        // 대상 USERID 모아오기 -> 기존 메서드 재사용
        return Stream.concat(shareService.findSharedUserIdsByUser(authUser).stream(), Stream.of(authUser.getId()))
                .map(userId -> SharedScheduleDto.builder()
                        .userId(userId)
                        .name(userService.findByUserId(userId).getName())
                        .me(userId.equals(authUser.getId()))
                        .schedules(getScheduleByDay(AuthUser.of(userId), date))
                        .build())
                .collect(Collectors.toList());
    }

    public List<SharedScheduleDto> getSharedScheduleByWeek(AuthUser authUser, LocalDate startOfWeek) {
        return Stream.concat(shareService.findSharedUserIdsByUser(authUser).stream(), Stream.of(authUser.getId()))
                .map(userId -> SharedScheduleDto.builder()
                        .userId(userId)
                        .name(userService.findByUserId(userId).getName())
                        .me(userId.equals(authUser.getId()))
                        .schedules(getScheduleByWeek(AuthUser.of(userId), startOfWeek))
                        .build())
                .collect(Collectors.toList());
    }

    public List<SharedScheduleDto> getSharedScheduleByMonth(AuthUser authUser, YearMonth yearMonth) {
        return Stream.concat(shareService.findSharedUserIdsByUser(authUser).stream(), Stream.of(authUser.getId()))
                .map(userId -> SharedScheduleDto.builder()
                        .userId(userId)
                        .name(userService.findByUserId(userId).getName())
                        .me(userId.equals(authUser.getId()))
                        .schedules(getScheduleByMonth(AuthUser.of(userId), yearMonth))
                        .build())
                .collect(Collectors.toList());
    }
}
