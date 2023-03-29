package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.repository.EngagementRepository;
import org.example.core.domain.entity.repository.ScheduleRepository;
import org.example.core.util.Period;
import org.example.dto.AuthUser;
import org.example.dto.ScheduleDto;
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
}
